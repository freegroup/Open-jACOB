// wmitop.cpp
//
// Revision history:
//  1.0 - initial revision
//

#include "stdafx.h"

typedef struct _PROCINFO * PPROCINFO;
typedef struct _PROCINFO {
	PPROCINFO	pNext;				  // pointer to the next process
	DWORDLONG	dwlKernelTime;		  // kernel time in 100-nanosecond intervals
	DWORDLONG	dwlUserTime;		  // user time in 100-nanosecond intervals
	DWORD		dwProcessId;		  // process identifier
	WCHAR		szPathName[MAX_PATH]; // executable path
} PROCINFO;

HANDLE			_hStop;
OSVERSIONINFO	_osvi;

//---------------------------------------------------------------------------
// CtrlHandler
//
//  This is the console control handler function. Exits the program when
//	a control signal is received.
//
//  Parameters:
//	  dwCtrlType - signal type
//
//  Returns:
//	  TRUE, if the signal was handled, FALSE - otherwise.
//
BOOL
WINAPI
CtrlHandler(
	IN DWORD dwCtrlType
	)
{
	_UNUSED(dwCtrlType);

	_VERIFY(SetEvent(_hStop));
	return TRUE;
}

//---------------------------------------------------------------------------
// FreeProcInfoList
//
//  Frees a process information list.
//
//  Parameters:
//	  pList - pointer to the head of the list
//
//  Returns:
//	  no return value.
//
VOID
FreeProcInfoList(
	IN PPROCINFO pList
	)
{
	_ASSERTE(pList != NULL);

	PPROCINFO p;

	do
	{
		p = pList;
		pList = p->pNext;
		CoTaskMemFree(p);
	}
	while (pList != NULL);
}

//---------------------------------------------------------------------------
// DoConnect
//
//  Connects to WMI server on the specified machine.
//
//  Parameters:
//	  pszMachineName - name of the target machine or NULL for local machine
//	  ppServer		 - pointer to a variable that receives the interface
//					   pointer of the server
//
//  Returns:
//	  standard COM code.
//
HRESULT
DoConnect(
	IN LPCTSTR pszMachineName,
	OUT IWbemServices ** ppServer
	)
{
	_ASSERTE(ppServer != NULL);
	*ppServer = NULL;

	HRESULT hRes;
	IWbemLocator * pLocator = NULL;

	TCHAR szServer[256];
	BSTR bstrServer = NULL;

	for (;;)
	{
		// create a WBEM locator object
		hRes = CoCreateInstance(__uuidof(WbemLocator), NULL,
							CLSCTX_INPROC_SERVER, __uuidof(IWbemLocator),
							(PVOID *)&pLocator);
		if (FAILED(hRes))
			break;

		// prepare server name
		if (pszMachineName == NULL)
			lstrcpy(szServer, _T("root\\cimv2"));
		else
			wsprintf(szServer, _T("%s\\root\\cimv2"), pszMachineName);

#ifdef _UNICODE
		bstrServer = SysAllocString(szServer);
		if (bstrServer == NULL)
		{
			hRes = E_OUTOFMEMORY;
			break;
		}
#else
		int len = lstrlen(szServer);
		bstrServer = SysAllocStringLen(NULL, len);
		if (bstrServer == NULL)
		{
			hRes = E_OUTOFMEMORY;
			break;
		}
		MultiByteToWideChar(CP_ACP, 0, szServer, -1, bstrServer, len + 1);
#endif

		// connect to the services object on the specified machine
		hRes = pLocator->ConnectServer(bstrServer, NULL,
									   NULL, NULL, 0, NULL, NULL, 
									   ppServer);
		break;
	}

	if (bstrServer != NULL)
		SysFreeString(bstrServer);
	if (pLocator != NULL)
		pLocator->Release();

	return hRes;
}

//---------------------------------------------------------------------------
// GetStatistics
//
//  Retrieve process statistics
//
//  Parameters:
//	  pServer - WMI server interface pointer
//	  ppInfo  - pointer to a variable that receives the list of processes
//				along with their statistics
//
//  Returns:
//	  standard COM return code.
//
HRESULT
GetStatistics(
	IN IWbemServices * pServer,
	OUT PPROCINFO * ppInfo
	)
{
	_ASSERTE(pServer != NULL);
	_ASSERTE(ppInfo != NULL);

	*ppInfo = NULL;

	HRESULT hRes;
	IEnumWbemClassObject * pEnum = NULL;
	IWbemClassObject * pObject = NULL;

	PPROCINFO pInfo = NULL;
	PPROCINFO pList = NULL;
	ULONG ulCount;
	
	VARIANT var;
	VariantInit(&var);

	BSTR bstrName = SysAllocString(L"Win32_Process");
	if (bstrName == NULL)
		return E_OUTOFMEMORY;

	for (;;)
	{
		// create an enumerator of processes
		hRes = pServer->CreateInstanceEnum(bstrName,
									WBEM_FLAG_SHALLOW|WBEM_FLAG_FORWARD_ONLY,
									NULL, &pEnum);
		if (FAILED(hRes))
			break;

		// walk through all processes
		for (;;)
		{
			if (pObject != NULL)
			{
				pObject->Release();
				pObject = NULL;
			}

			if (pInfo != NULL)
			{
				CoTaskMemFree(pInfo);
				pInfo = NULL;
			}

			// get next process instance
			if (pEnum->Next(WBEM_INFINITE, 1, &pObject, &ulCount) != S_OK)
			{
				hRes = S_OK;
				break;
			}

			// allocate new process information structure
			pInfo = (PPROCINFO)CoTaskMemAlloc(sizeof(PROCINFO));
			if (pInfo == NULL)
			{
				hRes = E_OUTOFMEMORY;
				break;
			}

			// retrieve process identifier
			VariantClear(&var);
			hRes = pObject->Get(L"ProcessId", 0, &var, NULL, NULL);
			if (FAILED(hRes))
				continue;

			_ASSERTE(V_VT(&var) == VT_I4);
			pInfo->dwProcessId = V_I4(&var);

			// retrieve process executable path
			VariantClear(&var);
			hRes = pObject->Get(L"ExecutablePath", 0, &var, NULL, NULL);
			if (FAILED(hRes))
				continue;
			
			if (V_VT(&var) == VT_NULL)
			{
				hRes = pObject->Get(L"Name", 0, &var, NULL, NULL);
				if (FAILED(hRes))
					continue;
			}

			_ASSERTE(V_VT(&var) == VT_BSTR);
			lstrcpynW(pInfo->szPathName, V_BSTR(&var), MAX_PATH);

			// retrieve process kernel time
			VariantClear(&var);
			hRes = pObject->Get(L"KernelModeTime", 0, &var, NULL, NULL);
			if (FAILED(hRes))
				continue;

			_ASSERTE(V_VT(&var) == VT_BSTR);
			pInfo->dwlKernelTime = _wtoi64(V_BSTR(&var));

			// retrieve process user time
			VariantClear(&var);
			hRes = pObject->Get(L"UserModeTime", 0, &var, NULL, NULL);
			if (FAILED(hRes))
				continue;

			_ASSERTE(V_VT(&var) == VT_BSTR);
			pInfo->dwlUserTime = _wtoi64(V_BSTR(&var));

			// insert this item into the list
			pInfo->pNext = pList;
			pList = pInfo;
			pInfo = NULL;
		}

		break;
	}

	VariantClear(&var);
	SysFreeString(bstrName);

	if (FAILED(hRes) && pList != NULL)
	{
		FreeProcInfoList(pList);
		pList = NULL;
	}

	if (pObject != NULL)
		pObject->Release();
	if (pEnum != NULL)
		pEnum->Release();

	*ppInfo = pList;
	return hRes;
}


//---------------------------------------------------------------------------
// CompareProc
//
//  Compares two PROCINFO structure for use with qsort.
//
//  Parameters:
//	  pv1 - pointer to a pointer to the first structure
//	  pv2 - pointer to a pointer to the second structure
//
//  Returns:
//	  comparison result.
//
int
__cdecl
CompareProc(
	IN CONST VOID * pv1,
	IN CONST VOID * pv2
	)
{
	_ASSERTE(pv1 != NULL);
	_ASSERTE(pv2 != NULL);

	PPROCINFO pInfo1 = *(PPROCINFO *)pv1;
	PPROCINFO pInfo2 = *(PPROCINFO *)pv2;
	
	DWORDLONG dwlTotal1 = pInfo1->dwlKernelTime + pInfo1->dwlUserTime;
	DWORDLONG dwlTotal2 = pInfo2->dwlKernelTime + pInfo2->dwlUserTime;

	if (dwlTotal1 < dwlTotal2)
		return 1;
	else if (dwlTotal1 > dwlTotal2)
		return -1;
	else
		return 0;
}

//---------------------------------------------------------------------------
// DisplayTop
//
//  Displays the difference between two snapshots.
//
//  Parameters:
//	  hConsole - console handle
//	  pCurr    - current statistics
//	  pPrev    - previous statistics
//
//  Returns:
//	  no return value.
//
VOID
DisplayTop(
	IN PPROCINFO pCurr,
	IN PPROCINFO pPrev
	)
{
	_ASSERTE(hConsole != NULL);
	_ASSERTE(pCurr != NULL);
	_ASSERTE(pPrev != NULL);

	PPROCINFO rgTop[256];
	ULONG nCount = 0;

	
	DWORDLONG dwlTotalUser = 0;
	DWORDLONG dwlTotalKernel = 0;
	DWORDLONG dwlIdle = 0;

	PPROCINFO p;

	while (pCurr != NULL)
	{
		p = pPrev;
		while (p != NULL)
		{
			// find matching process in the previous statistics list
			if (p->dwProcessId == pCurr->dwProcessId)
				break;
			p = p->pNext;
		}

		if (p == NULL)
		{
			// this is a new process, put it into the array by itself
			p = pCurr;
		}
		else
		{
			p->dwlKernelTime = pCurr->dwlKernelTime - p->dwlKernelTime;
			p->dwlUserTime = pCurr->dwlUserTime - p->dwlUserTime;
		}

		dwlTotalKernel += p->dwlKernelTime;
		dwlTotalUser += p->dwlUserTime;

		if (p->dwProcessId == 0)
			dwlIdle = p->dwlKernelTime;

		if (nCount < countof(rgTop))
			rgTop[nCount++] = p;

		pCurr = pCurr->pNext;
	}

	qsort(rgTop, nCount, sizeof(PPROCINFO), CompareProc);

	LONG nTotal;
	LONG nUser;
	LONG nKernel;
	DWORDLONG dwlTotal = dwlTotalKernel + dwlTotalUser;

	// display total processor usage
	nTotal = (LONG)((dwlTotal - dwlIdle) * 10000 / dwlTotal);
	nKernel = (LONG)((dwlTotalKernel - dwlIdle) * 10000 / dwlTotal);
	nUser = (LONG)(dwlTotalUser * 10000 / dwlTotal);

	printf(_T("jACOB Windows Process Information\n"));
	printf(_T("Total: %3d.%02d%%   Kernel: %3d.%02d%%   User: %3d.%02d%%\n"),
			 nTotal / 100, nTotal % 100,
			 nKernel / 100, nKernel % 100,
			 nUser / 100, nUser % 100);

	// display header
	printf(_T("  Total  Kernel   User    PID   Process\n"));


	// display most consuming processes
	for (ULONG i = 0; i < nCount; i++)
	{
		p = rgTop[i];
		_ASSERTE(p != NULL);

		nTotal = (LONG)((p->dwlKernelTime + p->dwlUserTime) * 10000 / dwlTotal);
		nKernel = (LONG)(p->dwlKernelTime * 10000 / dwlTotal);
		nUser = (LONG)(p->dwlUserTime * 10000 / dwlTotal);
		

		printf(_T("%3d.%02d%% %3d.%02d%% %3d.%02d%% %5d %ls\n"),
				 nTotal / 100, nTotal % 100,
				 nKernel / 100, nKernel % 100,
				 nUser / 100, nUser % 100,
				 p->dwProcessId,
				 p->szPathName);
	}
}

//---------------------------------------------------------------------------
// _tmain
//
//  Program entry point.
//
//  Parameters:
//    none.
//
//  Returns:
//	  program's exit code.
//
int
_tmain()
{
	PPROCINFO pCurr = NULL;
	PPROCINFO pPrev = NULL;
	HRESULT hRes;

	IWbemServices * pServer = NULL;

	_osvi.dwOSVersionInfoSize = sizeof(_osvi);
	_VERIFY(GetVersionEx(&_osvi));

	// initialize COM library
	hRes = CoInitialize(NULL);
	if (FAILED(hRes))
	{
		printf(_T("CoInitialize failed with code 0x%08X\n"), hRes);
		return -1;
	}

	// initialize security -- required for WMI
	hRes = CoInitializeSecurity(
			  NULL,
			  -1,
			  NULL,
			  NULL,
			  RPC_C_AUTHN_LEVEL_CONNECT,
			  RPC_C_IMP_LEVEL_IMPERSONATE,
			  NULL,
			  EOAC_NONE,
			  0);
	if (FAILED(hRes))
	{
		printf(_T("CoInitializeSecurity failed with code 0x%08X\n"), hRes);
		CoUninitialize();
		return -1;
	}

	// connect with the WMI server
	hRes = DoConnect(NULL, &pServer);
	if (FAILED(hRes))
	{
		printf(_T("Failed to connect to WMI server (0x%08X)\n"), hRes);
		CoUninitialize();
		return -1;
	}


	// obtain initial statistics
	hRes = GetStatistics(pServer, &pCurr);
	if (FAILED(hRes))
	{
		pServer->Release();
		CoUninitialize();
		return -1;
	}

		if (pPrev != NULL)
			FreeProcInfoList(pPrev);
		pPrev = pCurr;

      // Das System muss sich erstmal beruhigen. Das laden des Programms 'top.exe' hat die CPU belastet( DLL, Lib init....)
      //
      Sleep(1000);
		hRes = GetStatistics(pServer, &pCurr);

		// display the difference
		DisplayTop(pCurr, pPrev);

	pServer->Release();

	if (pPrev != NULL)
		FreeProcInfoList(pPrev);
	if (pCurr != NULL)
		FreeProcInfoList(pCurr);

	CloseHandle(_hStop);

	CoUninitialize();
	return 0;
}