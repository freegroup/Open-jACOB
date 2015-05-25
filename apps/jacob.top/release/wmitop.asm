	TITLE	J:\jacob.top\wmitop.cpp
	.386P
include listing.inc
if @Version gt 510
.model FLAT
else
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
_BSS	SEGMENT PARA USE32 PUBLIC 'BSS'
_BSS	ENDS
$$SYMBOLS	SEGMENT BYTE USE32 'DEBSYM'
$$SYMBOLS	ENDS
$$TYPES	SEGMENT BYTE USE32 'DEBTYP'
$$TYPES	ENDS
_TLS	SEGMENT DWORD USE32 PUBLIC 'TLS'
_TLS	ENDS
;	COMDAT ??_C@_0L@KKFB@root?2cimv2?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0O@MJBO@?$CFs?2root?2cimv2?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_1BM@DIEM@?$AAW?$AAi?$AAn?$AA3?$AA2?$AA_?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_1BE@KCJD@?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AAI?$AAd?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_1BO@EHNP@?$AAE?$AAx?$AAe?$AAc?$AAu?$AAt?$AAa?$AAb?$AAl?$AAe?$AAP?$AAa?$AAt?$AAh?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_19KFOE@?$AAN?$AAa?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_1BO@BOJK@?$AAK?$AAe?$AAr?$AAn?$AAe?$AAl?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_1BK@KFHD@?$AAU?$AAs?$AAe?$AAr?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CD@KEMP@jACOB?5Windows?5Process?5Informatio@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0DL@MHFB@Total?3?5?$CF3d?4?$CF02d?$CF?$CF?5?5?5Kernel?3?5?$CF3d?4@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CJ@PJJL@?5?5Total?5?5Kernel?5?5?5User?5?5?5?5PID?5?5?5@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CK@KBOP@?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CG@JJCH@CoInitialize?5failed?5with?5code?50x@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CO@KLPG@CoInitializeSecurity?5failed?5with@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0CK@JBDG@Failed?5to?5connect?5to?5WMI?5server?5@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT __GUID_4590f811_1d3a_11d0_891f_00aa004b2e24
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT __GUID_dc12a687_737f_11cf_884d_00aa004b2e24
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
FLAT	GROUP _DATA, CONST, _BSS
	ASSUME	CS: FLAT, DS: FLAT, SS: FLAT
endif
PUBLIC	?_hStop@@3PAXA					; _hStop
PUBLIC	?_osvi@@3U_OSVERSIONINFOA@@A			; _osvi
_BSS	SEGMENT
?_hStop@@3PAXA DD 01H DUP (?)				; _hStop
?_osvi@@3U_OSVERSIONINFOA@@A DB 094H DUP (?)		; _osvi
_BSS	ENDS
PUBLIC	?CtrlHandler@@YGHK@Z				; CtrlHandler
EXTRN	__imp__SetEvent@4:NEAR
_TEXT	SEGMENT
?CtrlHandler@@YGHK@Z PROC NEAR				; CtrlHandler

; 38   : {

	push	ebp
	mov	ebp, esp

; 39   : 	_UNUSED(dwCtrlType);
; 40   : 
; 41   : 	_VERIFY(SetEvent(_hStop));

	mov	eax, DWORD PTR ?_hStop@@3PAXA		; _hStop
	push	eax
	call	DWORD PTR __imp__SetEvent@4

; 42   : 	return TRUE;

	mov	eax, 1

; 43   : }

	pop	ebp
	ret	4
?CtrlHandler@@YGHK@Z ENDP				; CtrlHandler
_TEXT	ENDS
PUBLIC	?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z		; FreeProcInfoList
EXTRN	__imp__CoTaskMemFree@4:NEAR
_TEXT	SEGMENT
_pList$ = 8
_p$ = -4
?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z PROC NEAR	; FreeProcInfoList

; 60   : {

	push	ebp
	mov	ebp, esp
	push	ecx
$L48652:

; 67   : 		p = pList;

	mov	eax, DWORD PTR _pList$[ebp]
	mov	DWORD PTR _p$[ebp], eax

; 68   : 		pList = p->pNext;

	mov	ecx, DWORD PTR _p$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	DWORD PTR _pList$[ebp], edx

; 69   : 		CoTaskMemFree(p);

	mov	eax, DWORD PTR _p$[ebp]
	push	eax
	call	DWORD PTR __imp__CoTaskMemFree@4

; 71   : 	while (pList != NULL);

	cmp	DWORD PTR _pList$[ebp], 0
	jne	SHORT $L48652

; 72   : }

	mov	esp, ebp
	pop	ebp
	ret	0
?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z ENDP		; FreeProcInfoList
_TEXT	ENDS
PUBLIC	?DoConnect@@YAJPBDPAPAUIWbemServices@@@Z	; DoConnect
PUBLIC	__GUID_4590f811_1d3a_11d0_891f_00aa004b2e24
PUBLIC	__GUID_dc12a687_737f_11cf_884d_00aa004b2e24
PUBLIC	??_C@_0L@KKFB@root?2cimv2?$AA@			; `string'
PUBLIC	??_C@_0O@MJBO@?$CFs?2root?2cimv2?$AA@		; `string'
EXTRN	__imp__MultiByteToWideChar@24:NEAR
EXTRN	__imp__SysAllocStringLen@8:NEAR
EXTRN	__imp__SysFreeString@4:NEAR
EXTRN	__imp__CoCreateInstance@20:NEAR
EXTRN	__imp__lstrcpyA@8:NEAR
EXTRN	__imp__lstrlenA@4:NEAR
EXTRN	__imp__wsprintfA:NEAR
;	COMDAT __GUID_4590f811_1d3a_11d0_891f_00aa004b2e24
; File J:\jacob.top\wmitop.cpp
_DATA	SEGMENT
__GUID_4590f811_1d3a_11d0_891f_00aa004b2e24 DD 04590f811H
	DW	01d3aH
	DW	011d0H
	DB	089H
	DB	01fH
	DB	00H
	DB	0aaH
	DB	00H
	DB	04bH
	DB	02eH
	DB	024H
_DATA	ENDS
;	COMDAT __GUID_dc12a687_737f_11cf_884d_00aa004b2e24
_DATA	SEGMENT
__GUID_dc12a687_737f_11cf_884d_00aa004b2e24 DD 0dc12a687H
	DW	0737fH
	DW	011cfH
	DB	088H
	DB	04dH
	DB	00H
	DB	0aaH
	DB	00H
	DB	04bH
	DB	02eH
	DB	024H
_DATA	ENDS
;	COMDAT ??_C@_0L@KKFB@root?2cimv2?$AA@
CONST	SEGMENT
??_C@_0L@KKFB@root?2cimv2?$AA@ DB 'root\cimv2', 00H	; `string'
CONST	ENDS
;	COMDAT ??_C@_0O@MJBO@?$CFs?2root?2cimv2?$AA@
CONST	SEGMENT
??_C@_0O@MJBO@?$CFs?2root?2cimv2?$AA@ DB '%s\root\cimv2', 00H ; `string'
CONST	ENDS
_TEXT	SEGMENT
_pszMachineName$ = 8
_ppServer$ = 12
_hRes$ = -12
_pLocator$ = -4
_szServer$ = -268
_bstrServer$ = -8
_len$48676 = -272
?DoConnect@@YAJPBDPAPAUIWbemServices@@@Z PROC NEAR	; DoConnect

; 92   : {

	push	ebp
	mov	ebp, esp
	sub	esp, 272				; 00000110H

; 93   : 	_ASSERTE(ppServer != NULL);
; 94   : 	*ppServer = NULL;

	mov	eax, DWORD PTR _ppServer$[ebp]
	mov	DWORD PTR [eax], 0

; 95   : 
; 96   : 	HRESULT hRes;
; 97   : 	IWbemLocator * pLocator = NULL;

	mov	DWORD PTR _pLocator$[ebp], 0

; 98   : 
; 99   : 	TCHAR szServer[256];
; 100  : 	BSTR bstrServer = NULL;

	mov	DWORD PTR _bstrServer$[ebp], 0

; 104  : 		// create a WBEM locator object
; 105  : 		hRes = CoCreateInstance(__uuidof(WbemLocator), NULL,
; 106  : 							CLSCTX_INPROC_SERVER, __uuidof(IWbemLocator),
; 107  : 							(PVOID *)&pLocator);

	lea	ecx, DWORD PTR _pLocator$[ebp]
	push	ecx
	push	OFFSET FLAT:__GUID_dc12a687_737f_11cf_884d_00aa004b2e24
	push	1
	push	0
	push	OFFSET FLAT:__GUID_4590f811_1d3a_11d0_891f_00aa004b2e24
	call	DWORD PTR __imp__CoCreateInstance@20
	mov	DWORD PTR _hRes$[ebp], eax

; 108  : 		if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48671

; 109  : 			break;

	jmp	$L48666
$L48671:

; 110  : 
; 111  : 		// prepare server name
; 112  : 		if (pszMachineName == NULL)

	cmp	DWORD PTR _pszMachineName$[ebp], 0
	jne	SHORT $L48672

; 113  : 			lstrcpy(szServer, _T("root\\cimv2"));

	push	OFFSET FLAT:??_C@_0L@KKFB@root?2cimv2?$AA@ ; `string'
	lea	edx, DWORD PTR _szServer$[ebp]
	push	edx
	call	DWORD PTR __imp__lstrcpyA@8

; 114  : 		else

	jmp	SHORT $L48674
$L48672:

; 115  : 			wsprintf(szServer, _T("%s\\root\\cimv2"), pszMachineName);

	mov	eax, DWORD PTR _pszMachineName$[ebp]
	push	eax
	push	OFFSET FLAT:??_C@_0O@MJBO@?$CFs?2root?2cimv2?$AA@ ; `string'
	lea	ecx, DWORD PTR _szServer$[ebp]
	push	ecx
	call	DWORD PTR __imp__wsprintfA
	add	esp, 12					; 0000000cH
$L48674:

; 116  : 
; 117  : #ifdef _UNICODE
; 118  : 		bstrServer = SysAllocString(szServer);
; 119  : 		if (bstrServer == NULL)
; 120  : 		{
; 121  : 			hRes = E_OUTOFMEMORY;
; 122  : 			break;
; 123  : 		}
; 124  : #else
; 125  : 		int len = lstrlen(szServer);

	lea	edx, DWORD PTR _szServer$[ebp]
	push	edx
	call	DWORD PTR __imp__lstrlenA@4
	mov	DWORD PTR _len$48676[ebp], eax

; 126  : 		bstrServer = SysAllocStringLen(NULL, len);

	mov	eax, DWORD PTR _len$48676[ebp]
	push	eax
	push	0
	call	DWORD PTR __imp__SysAllocStringLen@8
	mov	DWORD PTR _bstrServer$[ebp], eax

; 127  : 		if (bstrServer == NULL)

	cmp	DWORD PTR _bstrServer$[ebp], 0
	jne	SHORT $L48677

; 129  : 			hRes = E_OUTOFMEMORY;

	mov	DWORD PTR _hRes$[ebp], -2147024882	; 8007000eH

; 130  : 			break;

	jmp	SHORT $L48666
$L48677:

; 132  : 		MultiByteToWideChar(CP_ACP, 0, szServer, -1, bstrServer, len + 1);

	mov	ecx, DWORD PTR _len$48676[ebp]
	add	ecx, 1
	push	ecx
	mov	edx, DWORD PTR _bstrServer$[ebp]
	push	edx
	push	-1
	lea	eax, DWORD PTR _szServer$[ebp]
	push	eax
	push	0
	push	0
	call	DWORD PTR __imp__MultiByteToWideChar@24

; 133  : #endif
; 134  : 
; 135  : 		// connect to the services object on the specified machine
; 136  : 		hRes = pLocator->ConnectServer(bstrServer, NULL,
; 137  : 									   NULL, NULL, 0, NULL, NULL, 
; 138  : 									   ppServer);

	mov	ecx, DWORD PTR _ppServer$[ebp]
	push	ecx
	push	0
	push	0
	push	0
	push	0
	push	0
	push	0
	mov	edx, DWORD PTR _bstrServer$[ebp]
	push	edx
	mov	eax, DWORD PTR _pLocator$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	edx, DWORD PTR _pLocator$[ebp]
	push	edx
	call	DWORD PTR [ecx+12]
	mov	DWORD PTR _hRes$[ebp], eax
$L48666:

; 141  : 
; 142  : 	if (bstrServer != NULL)

	cmp	DWORD PTR _bstrServer$[ebp], 0
	je	SHORT $L48679

; 143  : 		SysFreeString(bstrServer);

	mov	eax, DWORD PTR _bstrServer$[ebp]
	push	eax
	call	DWORD PTR __imp__SysFreeString@4
$L48679:

; 144  : 	if (pLocator != NULL)

	cmp	DWORD PTR _pLocator$[ebp], 0
	je	SHORT $L48680

; 145  : 		pLocator->Release();

	mov	ecx, DWORD PTR _pLocator$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pLocator$[ebp]
	push	eax
	call	DWORD PTR [edx+8]
$L48680:

; 146  : 
; 147  : 	return hRes;

	mov	eax, DWORD PTR _hRes$[ebp]

; 148  : }

	mov	esp, ebp
	pop	ebp
	ret	0
?DoConnect@@YAJPBDPAPAUIWbemServices@@@Z ENDP		; DoConnect
_TEXT	ENDS
PUBLIC	?GetStatistics@@YAJPAUIWbemServices@@PAPAU_PROCINFO@@@Z ; GetStatistics
PUBLIC	??_C@_1BM@DIEM@?$AAW?$AAi?$AAn?$AA3?$AA2?$AA_?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AA?$AA@ ; `string'
PUBLIC	??_C@_1BE@KCJD@?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AAI?$AAd?$AA?$AA@ ; `string'
PUBLIC	??_C@_1BO@EHNP@?$AAE?$AAx?$AAe?$AAc?$AAu?$AAt?$AAa?$AAb?$AAl?$AAe?$AAP?$AAa?$AAt?$AAh?$AA?$AA@ ; `string'
PUBLIC	??_C@_19KFOE@?$AAN?$AAa?$AAm?$AAe?$AA?$AA@	; `string'
PUBLIC	??_C@_1BO@BOJK@?$AAK?$AAe?$AAr?$AAn?$AAe?$AAl?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ ; `string'
PUBLIC	??_C@_1BK@KFHD@?$AAU?$AAs?$AAe?$AAr?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ ; `string'
EXTRN	__imp__SysAllocString@4:NEAR
EXTRN	__imp__VariantInit@4:NEAR
EXTRN	__imp__VariantClear@4:NEAR
EXTRN	__imp__CoTaskMemAlloc@4:NEAR
EXTRN	__imp__lstrcpynW@12:NEAR
EXTRN	__wtoi64:NEAR
;	COMDAT ??_C@_1BM@DIEM@?$AAW?$AAi?$AAn?$AA3?$AA2?$AA_?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AA?$AA@
; File J:\jacob.top\wmitop.cpp
CONST	SEGMENT
??_C@_1BM@DIEM@?$AAW?$AAi?$AAn?$AA3?$AA2?$AA_?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AA?$AA@ DB 'W'
	DB	00H, 'i', 00H, 'n', 00H, '3', 00H, '2', 00H, '_', 00H, 'P', 00H
	DB	'r', 00H, 'o', 00H, 'c', 00H, 'e', 00H, 's', 00H, 's', 00H, 00H
	DB	00H						; `string'
CONST	ENDS
;	COMDAT ??_C@_1BE@KCJD@?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AAI?$AAd?$AA?$AA@
CONST	SEGMENT
??_C@_1BE@KCJD@?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AAI?$AAd?$AA?$AA@ DB 'P'
	DB	00H, 'r', 00H, 'o', 00H, 'c', 00H, 'e', 00H, 's', 00H, 's', 00H
	DB	'I', 00H, 'd', 00H, 00H, 00H			; `string'
CONST	ENDS
;	COMDAT ??_C@_1BO@EHNP@?$AAE?$AAx?$AAe?$AAc?$AAu?$AAt?$AAa?$AAb?$AAl?$AAe?$AAP?$AAa?$AAt?$AAh?$AA?$AA@
CONST	SEGMENT
??_C@_1BO@EHNP@?$AAE?$AAx?$AAe?$AAc?$AAu?$AAt?$AAa?$AAb?$AAl?$AAe?$AAP?$AAa?$AAt?$AAh?$AA?$AA@ DB 'E'
	DB	00H, 'x', 00H, 'e', 00H, 'c', 00H, 'u', 00H, 't', 00H, 'a', 00H
	DB	'b', 00H, 'l', 00H, 'e', 00H, 'P', 00H, 'a', 00H, 't', 00H, 'h'
	DB	00H, 00H, 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_19KFOE@?$AAN?$AAa?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT
??_C@_19KFOE@?$AAN?$AAa?$AAm?$AAe?$AA?$AA@ DB 'N', 00H, 'a', 00H, 'm', 00H
	DB	'e', 00H, 00H, 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_1BO@BOJK@?$AAK?$AAe?$AAr?$AAn?$AAe?$AAl?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT
??_C@_1BO@BOJK@?$AAK?$AAe?$AAr?$AAn?$AAe?$AAl?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ DB 'K'
	DB	00H, 'e', 00H, 'r', 00H, 'n', 00H, 'e', 00H, 'l', 00H, 'M', 00H
	DB	'o', 00H, 'd', 00H, 'e', 00H, 'T', 00H, 'i', 00H, 'm', 00H, 'e'
	DB	00H, 00H, 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_1BK@KFHD@?$AAU?$AAs?$AAe?$AAr?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@
CONST	SEGMENT
??_C@_1BK@KFHD@?$AAU?$AAs?$AAe?$AAr?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ DB 'U'
	DB	00H, 's', 00H, 'e', 00H, 'r', 00H, 'M', 00H, 'o', 00H, 'd', 00H
	DB	'e', 00H, 'T', 00H, 'i', 00H, 'm', 00H, 'e', 00H, 00H, 00H ; `string'
CONST	ENDS
_TEXT	SEGMENT
_pServer$ = 8
_ppInfo$ = 12
_hRes$ = -36
_pEnum$ = -40
_pObject$ = -44
_pInfo$ = -32
_pList$ = -28
_ulCount$ = -4
_var$ = -24
_bstrName$ = -8
?GetStatistics@@YAJPAUIWbemServices@@PAPAU_PROCINFO@@@Z PROC NEAR ; GetStatistics

; 168  : {

	push	ebp
	mov	ebp, esp
	sub	esp, 44					; 0000002cH

; 169  : 	_ASSERTE(pServer != NULL);
; 170  : 	_ASSERTE(ppInfo != NULL);
; 171  : 
; 172  : 	*ppInfo = NULL;

	mov	eax, DWORD PTR _ppInfo$[ebp]
	mov	DWORD PTR [eax], 0

; 173  : 
; 174  : 	HRESULT hRes;
; 175  : 	IEnumWbemClassObject * pEnum = NULL;

	mov	DWORD PTR _pEnum$[ebp], 0

; 176  : 	IWbemClassObject * pObject = NULL;

	mov	DWORD PTR _pObject$[ebp], 0

; 177  : 
; 178  : 	PPROCINFO pInfo = NULL;

	mov	DWORD PTR _pInfo$[ebp], 0

; 179  : 	PPROCINFO pList = NULL;

	mov	DWORD PTR _pList$[ebp], 0

; 180  : 	ULONG ulCount;
; 181  : 	
; 182  : 	VARIANT var;
; 183  : 	VariantInit(&var);

	lea	ecx, DWORD PTR _var$[ebp]
	push	ecx
	call	DWORD PTR __imp__VariantInit@4

; 184  : 
; 185  : 	BSTR bstrName = SysAllocString(L"Win32_Process");

	push	OFFSET FLAT:??_C@_1BM@DIEM@?$AAW?$AAi?$AAn?$AA3?$AA2?$AA_?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AA?$AA@ ; `string'
	call	DWORD PTR __imp__SysAllocString@4
	mov	DWORD PTR _bstrName$[ebp], eax

; 186  : 	if (bstrName == NULL)

	cmp	DWORD PTR _bstrName$[ebp], 0
	jne	SHORT $L48696

; 187  : 		return E_OUTOFMEMORY;

	mov	eax, -2147024882			; 8007000eH
	jmp	$L48684
$L48696:

; 191  : 		// create an enumerator of processes
; 192  : 		hRes = pServer->CreateInstanceEnum(bstrName,
; 193  : 									WBEM_FLAG_SHALLOW|WBEM_FLAG_FORWARD_ONLY,
; 194  : 									NULL, &pEnum);

	lea	edx, DWORD PTR _pEnum$[ebp]
	push	edx
	push	0
	push	33					; 00000021H
	mov	eax, DWORD PTR _bstrName$[ebp]
	push	eax
	mov	ecx, DWORD PTR _pServer$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pServer$[ebp]
	push	eax
	call	DWORD PTR [edx+72]
	mov	DWORD PTR _hRes$[ebp], eax

; 195  : 		if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48702

; 196  : 			break;

	jmp	$L48700
$L48702:

; 201  : 			if (pObject != NULL)

	cmp	DWORD PTR _pObject$[ebp], 0
	je	SHORT $L48706

; 203  : 				pObject->Release();

	mov	ecx, DWORD PTR _pObject$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pObject$[ebp]
	push	eax
	call	DWORD PTR [edx+8]

; 204  : 				pObject = NULL;

	mov	DWORD PTR _pObject$[ebp], 0
$L48706:

; 206  : 
; 207  : 			if (pInfo != NULL)

	cmp	DWORD PTR _pInfo$[ebp], 0
	je	SHORT $L48707

; 209  : 				CoTaskMemFree(pInfo);

	mov	ecx, DWORD PTR _pInfo$[ebp]
	push	ecx
	call	DWORD PTR __imp__CoTaskMemFree@4

; 210  : 				pInfo = NULL;

	mov	DWORD PTR _pInfo$[ebp], 0
$L48707:

; 212  : 
; 213  : 			// get next process instance
; 214  : 			if (pEnum->Next(WBEM_INFINITE, 1, &pObject, &ulCount) != S_OK)

	lea	edx, DWORD PTR _ulCount$[ebp]
	push	edx
	lea	eax, DWORD PTR _pObject$[ebp]
	push	eax
	push	1
	push	-1
	mov	ecx, DWORD PTR _pEnum$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pEnum$[ebp]
	push	eax
	call	DWORD PTR [edx+16]
	test	eax, eax
	je	SHORT $L48709

; 216  : 				hRes = S_OK;

	mov	DWORD PTR _hRes$[ebp], 0

; 217  : 				break;

	jmp	$L48705
$L48709:

; 219  : 
; 220  : 			// allocate new process information structure
; 221  : 			pInfo = (PPROCINFO)CoTaskMemAlloc(sizeof(PROCINFO));

	push	552					; 00000228H
	call	DWORD PTR __imp__CoTaskMemAlloc@4
	mov	DWORD PTR _pInfo$[ebp], eax

; 222  : 			if (pInfo == NULL)

	cmp	DWORD PTR _pInfo$[ebp], 0
	jne	SHORT $L48713

; 224  : 				hRes = E_OUTOFMEMORY;

	mov	DWORD PTR _hRes$[ebp], -2147024882	; 8007000eH

; 225  : 				break;

	jmp	$L48705
$L48713:

; 227  : 
; 228  : 			// retrieve process identifier
; 229  : 			VariantClear(&var);

	lea	ecx, DWORD PTR _var$[ebp]
	push	ecx
	call	DWORD PTR __imp__VariantClear@4

; 230  : 			hRes = pObject->Get(L"ProcessId", 0, &var, NULL, NULL);

	push	0
	push	0
	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	push	0
	push	OFFSET FLAT:??_C@_1BE@KCJD@?$AAP?$AAr?$AAo?$AAc?$AAe?$AAs?$AAs?$AAI?$AAd?$AA?$AA@ ; `string'
	mov	eax, DWORD PTR _pObject$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	edx, DWORD PTR _pObject$[ebp]
	push	edx
	call	DWORD PTR [ecx+16]
	mov	DWORD PTR _hRes$[ebp], eax

; 231  : 			if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48717

; 232  : 				continue;

	jmp	$L48702
$L48717:

; 235  : 			pInfo->dwProcessId = V_I4(&var);

	mov	eax, DWORD PTR _pInfo$[ebp]
	mov	ecx, DWORD PTR _var$[ebp+8]
	mov	DWORD PTR [eax+24], ecx

; 236  : 
; 237  : 			// retrieve process executable path
; 238  : 			VariantClear(&var);

	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	call	DWORD PTR __imp__VariantClear@4

; 239  : 			hRes = pObject->Get(L"ExecutablePath", 0, &var, NULL, NULL);

	push	0
	push	0
	lea	eax, DWORD PTR _var$[ebp]
	push	eax
	push	0
	push	OFFSET FLAT:??_C@_1BO@EHNP@?$AAE?$AAx?$AAe?$AAc?$AAu?$AAt?$AAa?$AAb?$AAl?$AAe?$AAP?$AAa?$AAt?$AAh?$AA?$AA@ ; `string'
	mov	ecx, DWORD PTR _pObject$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pObject$[ebp]
	push	eax
	call	DWORD PTR [edx+16]
	mov	DWORD PTR _hRes$[ebp], eax

; 240  : 			if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48721

; 241  : 				continue;

	jmp	$L48702
$L48721:

; 242  : 			
; 243  : 			if (V_VT(&var) == VT_NULL)

	mov	ecx, DWORD PTR _var$[ebp]
	and	ecx, 65535				; 0000ffffH
	cmp	ecx, 1
	jne	SHORT $L48725

; 245  : 				hRes = pObject->Get(L"Name", 0, &var, NULL, NULL);

	push	0
	push	0
	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	push	0
	push	OFFSET FLAT:??_C@_19KFOE@?$AAN?$AAa?$AAm?$AAe?$AA?$AA@ ; `string'
	mov	eax, DWORD PTR _pObject$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	edx, DWORD PTR _pObject$[ebp]
	push	edx
	call	DWORD PTR [ecx+16]
	mov	DWORD PTR _hRes$[ebp], eax

; 246  : 				if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48725

; 247  : 					continue;

	jmp	$L48702
$L48725:

; 251  : 			lstrcpynW(pInfo->szPathName, V_BSTR(&var), MAX_PATH);

	push	260					; 00000104H
	mov	eax, DWORD PTR _var$[ebp+8]
	push	eax
	mov	ecx, DWORD PTR _pInfo$[ebp]
	add	ecx, 28					; 0000001cH
	push	ecx
	call	DWORD PTR __imp__lstrcpynW@12

; 252  : 
; 253  : 			// retrieve process kernel time
; 254  : 			VariantClear(&var);

	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	call	DWORD PTR __imp__VariantClear@4

; 255  : 			hRes = pObject->Get(L"KernelModeTime", 0, &var, NULL, NULL);

	push	0
	push	0
	lea	eax, DWORD PTR _var$[ebp]
	push	eax
	push	0
	push	OFFSET FLAT:??_C@_1BO@BOJK@?$AAK?$AAe?$AAr?$AAn?$AAe?$AAl?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ ; `string'
	mov	ecx, DWORD PTR _pObject$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pObject$[ebp]
	push	eax
	call	DWORD PTR [edx+16]
	mov	DWORD PTR _hRes$[ebp], eax

; 256  : 			if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48729

; 257  : 				continue;

	jmp	$L48702
$L48729:

; 260  : 			pInfo->dwlKernelTime = _wtoi64(V_BSTR(&var));

	mov	ecx, DWORD PTR _var$[ebp+8]
	push	ecx
	call	__wtoi64
	add	esp, 4
	mov	ecx, DWORD PTR _pInfo$[ebp]
	mov	DWORD PTR [ecx+8], eax
	mov	DWORD PTR [ecx+12], edx

; 261  : 
; 262  : 			// retrieve process user time
; 263  : 			VariantClear(&var);

	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	call	DWORD PTR __imp__VariantClear@4

; 264  : 			hRes = pObject->Get(L"UserModeTime", 0, &var, NULL, NULL);

	push	0
	push	0
	lea	eax, DWORD PTR _var$[ebp]
	push	eax
	push	0
	push	OFFSET FLAT:??_C@_1BK@KFHD@?$AAU?$AAs?$AAe?$AAr?$AAM?$AAo?$AAd?$AAe?$AAT?$AAi?$AAm?$AAe?$AA?$AA@ ; `string'
	mov	ecx, DWORD PTR _pObject$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	eax, DWORD PTR _pObject$[ebp]
	push	eax
	call	DWORD PTR [edx+16]
	mov	DWORD PTR _hRes$[ebp], eax

; 265  : 			if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48733

; 266  : 				continue;

	jmp	$L48702
$L48733:

; 269  : 			pInfo->dwlUserTime = _wtoi64(V_BSTR(&var));

	mov	ecx, DWORD PTR _var$[ebp+8]
	push	ecx
	call	__wtoi64
	add	esp, 4
	mov	ecx, DWORD PTR _pInfo$[ebp]
	mov	DWORD PTR [ecx+16], eax
	mov	DWORD PTR [ecx+20], edx

; 270  : 
; 271  : 			// insert this item into the list
; 272  : 			pInfo->pNext = pList;

	mov	edx, DWORD PTR _pInfo$[ebp]
	mov	eax, DWORD PTR _pList$[ebp]
	mov	DWORD PTR [edx], eax

; 273  : 			pList = pInfo;

	mov	ecx, DWORD PTR _pInfo$[ebp]
	mov	DWORD PTR _pList$[ebp], ecx

; 274  : 			pInfo = NULL;

	mov	DWORD PTR _pInfo$[ebp], 0

; 275  : 		}

	jmp	$L48702
$L48705:
$L48700:

; 279  : 
; 280  : 	VariantClear(&var);

	lea	edx, DWORD PTR _var$[ebp]
	push	edx
	call	DWORD PTR __imp__VariantClear@4

; 281  : 	SysFreeString(bstrName);

	mov	eax, DWORD PTR _bstrName$[ebp]
	push	eax
	call	DWORD PTR __imp__SysFreeString@4

; 282  : 
; 283  : 	if (FAILED(hRes) && pList != NULL)

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48736
	cmp	DWORD PTR _pList$[ebp], 0
	je	SHORT $L48736

; 285  : 		FreeProcInfoList(pList);

	mov	ecx, DWORD PTR _pList$[ebp]
	push	ecx
	call	?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z	; FreeProcInfoList
	add	esp, 4

; 286  : 		pList = NULL;

	mov	DWORD PTR _pList$[ebp], 0
$L48736:

; 288  : 
; 289  : 	if (pObject != NULL)

	cmp	DWORD PTR _pObject$[ebp], 0
	je	SHORT $L48737

; 290  : 		pObject->Release();

	mov	edx, DWORD PTR _pObject$[ebp]
	mov	eax, DWORD PTR [edx]
	mov	ecx, DWORD PTR _pObject$[ebp]
	push	ecx
	call	DWORD PTR [eax+8]
$L48737:

; 291  : 	if (pEnum != NULL)

	cmp	DWORD PTR _pEnum$[ebp], 0
	je	SHORT $L48738

; 292  : 		pEnum->Release();

	mov	edx, DWORD PTR _pEnum$[ebp]
	mov	eax, DWORD PTR [edx]
	mov	ecx, DWORD PTR _pEnum$[ebp]
	push	ecx
	call	DWORD PTR [eax+8]
$L48738:

; 293  : 
; 294  : 	*ppInfo = pList;

	mov	edx, DWORD PTR _ppInfo$[ebp]
	mov	eax, DWORD PTR _pList$[ebp]
	mov	DWORD PTR [edx], eax

; 295  : 	return hRes;

	mov	eax, DWORD PTR _hRes$[ebp]
$L48684:

; 296  : }

	mov	esp, ebp
	pop	ebp
	ret	0
?GetStatistics@@YAJPAUIWbemServices@@PAPAU_PROCINFO@@@Z ENDP ; GetStatistics
_TEXT	ENDS
PUBLIC	?CompareProc@@YAHPBX0@Z				; CompareProc
_TEXT	SEGMENT
_pv1$ = 8
_pv2$ = 12
_pInfo1$ = -20
_pInfo2$ = -24
_dwlTotal1$ = -16
_dwlTotal2$ = -8
?CompareProc@@YAHPBX0@Z PROC NEAR			; CompareProc

; 317  : {

	push	ebp
	mov	ebp, esp
	sub	esp, 24					; 00000018H

; 318  : 	_ASSERTE(pv1 != NULL);
; 319  : 	_ASSERTE(pv2 != NULL);
; 320  : 
; 321  : 	PPROCINFO pInfo1 = *(PPROCINFO *)pv1;

	mov	eax, DWORD PTR _pv1$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	DWORD PTR _pInfo1$[ebp], ecx

; 322  : 	PPROCINFO pInfo2 = *(PPROCINFO *)pv2;

	mov	edx, DWORD PTR _pv2$[ebp]
	mov	eax, DWORD PTR [edx]
	mov	DWORD PTR _pInfo2$[ebp], eax

; 323  : 	
; 324  : 	DWORDLONG dwlTotal1 = pInfo1->dwlKernelTime + pInfo1->dwlUserTime;

	mov	ecx, DWORD PTR _pInfo1$[ebp]
	mov	edx, DWORD PTR _pInfo1$[ebp]
	mov	eax, DWORD PTR [ecx+8]
	add	eax, DWORD PTR [edx+16]
	mov	ecx, DWORD PTR [ecx+12]
	adc	ecx, DWORD PTR [edx+20]
	mov	DWORD PTR _dwlTotal1$[ebp], eax
	mov	DWORD PTR _dwlTotal1$[ebp+4], ecx

; 325  : 	DWORDLONG dwlTotal2 = pInfo2->dwlKernelTime + pInfo2->dwlUserTime;

	mov	edx, DWORD PTR _pInfo2$[ebp]
	mov	eax, DWORD PTR _pInfo2$[ebp]
	mov	ecx, DWORD PTR [edx+8]
	add	ecx, DWORD PTR [eax+16]
	mov	edx, DWORD PTR [edx+12]
	adc	edx, DWORD PTR [eax+20]
	mov	DWORD PTR _dwlTotal2$[ebp], ecx
	mov	DWORD PTR _dwlTotal2$[ebp+4], edx

; 326  : 
; 327  : 	if (dwlTotal1 < dwlTotal2)

	mov	eax, DWORD PTR _dwlTotal1$[ebp+4]
	cmp	eax, DWORD PTR _dwlTotal2$[ebp+4]
	ja	SHORT $L48751
	jb	SHORT $L48864
	mov	ecx, DWORD PTR _dwlTotal1$[ebp]
	cmp	ecx, DWORD PTR _dwlTotal2$[ebp]
	jae	SHORT $L48751
$L48864:

; 328  : 		return 1;

	mov	eax, 1
	jmp	SHORT $L48754
$L48751:

; 329  : 	else if (dwlTotal1 > dwlTotal2)

	mov	edx, DWORD PTR _dwlTotal1$[ebp+4]
	cmp	edx, DWORD PTR _dwlTotal2$[ebp+4]
	jb	SHORT $L48753
	ja	SHORT $L48865
	mov	eax, DWORD PTR _dwlTotal1$[ebp]
	cmp	eax, DWORD PTR _dwlTotal2$[ebp]
	jbe	SHORT $L48753
$L48865:

; 330  : 		return -1;

	or	eax, -1
	jmp	SHORT $L48754
$L48753:

; 332  : 		return 0;

	xor	eax, eax
$L48754:

; 333  : }

	mov	esp, ebp
	pop	ebp
	ret	0
?CompareProc@@YAHPBX0@Z ENDP				; CompareProc
_TEXT	ENDS
PUBLIC	?DisplayTop@@YAXPAU_PROCINFO@@0@Z		; DisplayTop
PUBLIC	??_C@_0CD@KEMP@jACOB?5Windows?5Process?5Informatio@ ; `string'
PUBLIC	??_C@_0DL@MHFB@Total?3?5?$CF3d?4?$CF02d?$CF?$CF?5?5?5Kernel?3?5?$CF3d?4@ ; `string'
PUBLIC	??_C@_0CJ@PJJL@?5?5Total?5?5Kernel?5?5?5User?5?5?5?5PID?5?5?5@ ; `string'
PUBLIC	??_C@_0CK@KBOP@?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF@ ; `string'
EXTRN	_printf:NEAR
EXTRN	__allmul:NEAR
EXTRN	__aulldiv:NEAR
EXTRN	_qsort:NEAR
;	COMDAT ??_C@_0CD@KEMP@jACOB?5Windows?5Process?5Informatio@
; File J:\jacob.top\wmitop.cpp
CONST	SEGMENT
??_C@_0CD@KEMP@jACOB?5Windows?5Process?5Informatio@ DB 'jACOB Windows Pro'
	DB	'cess Information', 0aH, 00H			; `string'
CONST	ENDS
;	COMDAT ??_C@_0DL@MHFB@Total?3?5?$CF3d?4?$CF02d?$CF?$CF?5?5?5Kernel?3?5?$CF3d?4@
CONST	SEGMENT
??_C@_0DL@MHFB@Total?3?5?$CF3d?4?$CF02d?$CF?$CF?5?5?5Kernel?3?5?$CF3d?4@ DB 'T'
	DB	'otal: %3d.%02d%%   Kernel: %3d.%02d%%   User: %3d.%02d%%', 0aH
	DB	00H						; `string'
CONST	ENDS
;	COMDAT ??_C@_0CJ@PJJL@?5?5Total?5?5Kernel?5?5?5User?5?5?5?5PID?5?5?5@
CONST	SEGMENT
??_C@_0CJ@PJJL@?5?5Total?5?5Kernel?5?5?5User?5?5?5?5PID?5?5?5@ DB '  Tota'
	DB	'l  Kernel   User    PID   Process', 0aH, 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0CK@KBOP@?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF@
CONST	SEGMENT
??_C@_0CK@KBOP@?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF@ DB '%'
	DB	'3d.%02d%% %3d.%02d%% %3d.%02d%% %5d %ls', 0aH, 00H ; `string'
CONST	ENDS
_TEXT	SEGMENT
_pCurr$ = 8
_pPrev$ = 12
_rgTop$ = -1080
_nCount$ = -32
_dwlTotalUser$ = -40
_dwlTotalKernel$ = -16
_dwlIdle$ = -56
_p$ = -8
_nTotal$ = -24
_nUser$ = -20
_nKernel$ = -4
_dwlTotal$ = -48
_i$ = -28
?DisplayTop@@YAXPAU_PROCINFO@@0@Z PROC NEAR		; DisplayTop

; 353  : {

	push	ebp
	mov	ebp, esp
	sub	esp, 1080				; 00000438H

; 354  : 	_ASSERTE(hConsole != NULL);
; 355  : 	_ASSERTE(pCurr != NULL);
; 356  : 	_ASSERTE(pPrev != NULL);
; 357  : 
; 358  : 	PPROCINFO rgTop[256];
; 359  : 	ULONG nCount = 0;

	mov	DWORD PTR _nCount$[ebp], 0

; 360  : 
; 361  : 	
; 362  : 	DWORDLONG dwlTotalUser = 0;

	mov	DWORD PTR _dwlTotalUser$[ebp], 0
	mov	DWORD PTR _dwlTotalUser$[ebp+4], 0

; 363  : 	DWORDLONG dwlTotalKernel = 0;

	mov	DWORD PTR _dwlTotalKernel$[ebp], 0
	mov	DWORD PTR _dwlTotalKernel$[ebp+4], 0

; 364  : 	DWORDLONG dwlIdle = 0;

	mov	DWORD PTR _dwlIdle$[ebp], 0
	mov	DWORD PTR _dwlIdle$[ebp+4], 0
$L48769:

; 365  : 
; 366  : 	PPROCINFO p;
; 367  : 
; 368  : 	while (pCurr != NULL)

	cmp	DWORD PTR _pCurr$[ebp], 0
	je	$L48770

; 370  : 		p = pPrev;

	mov	eax, DWORD PTR _pPrev$[ebp]
	mov	DWORD PTR _p$[ebp], eax
$L48772:

; 371  : 		while (p != NULL)

	cmp	DWORD PTR _p$[ebp], 0
	je	SHORT $L48773

; 373  : 			// find matching process in the previous statistics list
; 374  : 			if (p->dwProcessId == pCurr->dwProcessId)

	mov	ecx, DWORD PTR _p$[ebp]
	mov	edx, DWORD PTR _pCurr$[ebp]
	mov	eax, DWORD PTR [ecx+24]
	cmp	eax, DWORD PTR [edx+24]
	jne	SHORT $L48774

; 375  : 				break;

	jmp	SHORT $L48773
$L48774:

; 376  : 			p = p->pNext;

	mov	ecx, DWORD PTR _p$[ebp]
	mov	edx, DWORD PTR [ecx]
	mov	DWORD PTR _p$[ebp], edx

; 377  : 		}

	jmp	SHORT $L48772
$L48773:

; 378  : 
; 379  : 		if (p == NULL)

	cmp	DWORD PTR _p$[ebp], 0
	jne	SHORT $L48775

; 381  : 			// this is a new process, put it into the array by itself
; 382  : 			p = pCurr;

	mov	eax, DWORD PTR _pCurr$[ebp]
	mov	DWORD PTR _p$[ebp], eax

; 384  : 		else

	jmp	SHORT $L48776
$L48775:

; 386  : 			p->dwlKernelTime = pCurr->dwlKernelTime - p->dwlKernelTime;

	mov	ecx, DWORD PTR _pCurr$[ebp]
	mov	edx, DWORD PTR _p$[ebp]
	mov	eax, DWORD PTR [ecx+8]
	sub	eax, DWORD PTR [edx+8]
	mov	ecx, DWORD PTR [ecx+12]
	sbb	ecx, DWORD PTR [edx+12]
	mov	edx, DWORD PTR _p$[ebp]
	mov	DWORD PTR [edx+8], eax
	mov	DWORD PTR [edx+12], ecx

; 387  : 			p->dwlUserTime = pCurr->dwlUserTime - p->dwlUserTime;

	mov	eax, DWORD PTR _pCurr$[ebp]
	mov	ecx, DWORD PTR _p$[ebp]
	mov	edx, DWORD PTR [eax+16]
	sub	edx, DWORD PTR [ecx+16]
	mov	eax, DWORD PTR [eax+20]
	sbb	eax, DWORD PTR [ecx+20]
	mov	ecx, DWORD PTR _p$[ebp]
	mov	DWORD PTR [ecx+16], edx
	mov	DWORD PTR [ecx+20], eax
$L48776:

; 389  : 
; 390  : 		dwlTotalKernel += p->dwlKernelTime;

	mov	edx, DWORD PTR _p$[ebp]
	mov	eax, DWORD PTR _dwlTotalKernel$[ebp]
	add	eax, DWORD PTR [edx+8]
	mov	ecx, DWORD PTR _dwlTotalKernel$[ebp+4]
	adc	ecx, DWORD PTR [edx+12]
	mov	DWORD PTR _dwlTotalKernel$[ebp], eax
	mov	DWORD PTR _dwlTotalKernel$[ebp+4], ecx

; 391  : 		dwlTotalUser += p->dwlUserTime;

	mov	edx, DWORD PTR _p$[ebp]
	mov	eax, DWORD PTR _dwlTotalUser$[ebp]
	add	eax, DWORD PTR [edx+16]
	mov	ecx, DWORD PTR _dwlTotalUser$[ebp+4]
	adc	ecx, DWORD PTR [edx+20]
	mov	DWORD PTR _dwlTotalUser$[ebp], eax
	mov	DWORD PTR _dwlTotalUser$[ebp+4], ecx

; 392  : 
; 393  : 		if (p->dwProcessId == 0)

	mov	edx, DWORD PTR _p$[ebp]
	cmp	DWORD PTR [edx+24], 0
	jne	SHORT $L48777

; 394  : 			dwlIdle = p->dwlKernelTime;

	mov	eax, DWORD PTR _p$[ebp]
	mov	ecx, DWORD PTR [eax+8]
	mov	DWORD PTR _dwlIdle$[ebp], ecx
	mov	edx, DWORD PTR [eax+12]
	mov	DWORD PTR _dwlIdle$[ebp+4], edx
$L48777:

; 395  : 
; 396  : 		if (nCount < countof(rgTop))

	cmp	DWORD PTR _nCount$[ebp], 256		; 00000100H
	jae	SHORT $L48778

; 397  : 			rgTop[nCount++] = p;

	mov	eax, DWORD PTR _nCount$[ebp]
	mov	ecx, DWORD PTR _p$[ebp]
	mov	DWORD PTR _rgTop$[ebp+eax*4], ecx
	mov	edx, DWORD PTR _nCount$[ebp]
	add	edx, 1
	mov	DWORD PTR _nCount$[ebp], edx
$L48778:

; 398  : 
; 399  : 		pCurr = pCurr->pNext;

	mov	eax, DWORD PTR _pCurr$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	DWORD PTR _pCurr$[ebp], ecx

; 400  : 	}

	jmp	$L48769
$L48770:

; 401  : 
; 402  : 	qsort(rgTop, nCount, sizeof(PPROCINFO), CompareProc);

	push	OFFSET FLAT:?CompareProc@@YAHPBX0@Z	; CompareProc
	push	4
	mov	edx, DWORD PTR _nCount$[ebp]
	push	edx
	lea	eax, DWORD PTR _rgTop$[ebp]
	push	eax
	call	_qsort
	add	esp, 16					; 00000010H

; 403  : 
; 404  : 	LONG nTotal;
; 405  : 	LONG nUser;
; 406  : 	LONG nKernel;
; 407  : 	DWORDLONG dwlTotal = dwlTotalKernel + dwlTotalUser;

	mov	ecx, DWORD PTR _dwlTotalKernel$[ebp]
	add	ecx, DWORD PTR _dwlTotalUser$[ebp]
	mov	edx, DWORD PTR _dwlTotalKernel$[ebp+4]
	adc	edx, DWORD PTR _dwlTotalUser$[ebp+4]
	mov	DWORD PTR _dwlTotal$[ebp], ecx
	mov	DWORD PTR _dwlTotal$[ebp+4], edx

; 408  : 
; 409  : 	// display total processor usage
; 410  : 	nTotal = (LONG)((dwlTotal - dwlIdle) * 10000 / dwlTotal);

	mov	eax, DWORD PTR _dwlTotal$[ebp]
	sub	eax, DWORD PTR _dwlIdle$[ebp]
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	sbb	ecx, DWORD PTR _dwlIdle$[ebp+4]
	push	0
	push	10000					; 00002710H
	push	ecx
	push	eax
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nTotal$[ebp], eax

; 411  : 	nKernel = (LONG)((dwlTotalKernel - dwlIdle) * 10000 / dwlTotal);

	mov	edx, DWORD PTR _dwlTotalKernel$[ebp]
	sub	edx, DWORD PTR _dwlIdle$[ebp]
	mov	eax, DWORD PTR _dwlTotalKernel$[ebp+4]
	sbb	eax, DWORD PTR _dwlIdle$[ebp+4]
	push	0
	push	10000					; 00002710H
	push	eax
	push	edx
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nKernel$[ebp], eax

; 412  : 	nUser = (LONG)(dwlTotalUser * 10000 / dwlTotal);

	push	0
	push	10000					; 00002710H
	mov	edx, DWORD PTR _dwlTotalUser$[ebp+4]
	push	edx
	mov	eax, DWORD PTR _dwlTotalUser$[ebp]
	push	eax
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nUser$[ebp], eax

; 413  : 
; 414  : 	printf(_T("jACOB Windows Process Information\n"));

	push	OFFSET FLAT:??_C@_0CD@KEMP@jACOB?5Windows?5Process?5Informatio@ ; `string'
	call	_printf
	add	esp, 4

; 415  : 	printf(_T("Total: %3d.%02d%%   Kernel: %3d.%02d%%   User: %3d.%02d%%\n"),
; 416  : 			 nTotal / 100, nTotal % 100,
; 417  : 			 nKernel / 100, nKernel % 100,
; 418  : 			 nUser / 100, nUser % 100);

	mov	eax, DWORD PTR _nUser$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nUser$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	mov	eax, DWORD PTR _nKernel$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nKernel$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	mov	eax, DWORD PTR _nTotal$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nTotal$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	push	OFFSET FLAT:??_C@_0DL@MHFB@Total?3?5?$CF3d?4?$CF02d?$CF?$CF?5?5?5Kernel?3?5?$CF3d?4@ ; `string'
	call	_printf
	add	esp, 28					; 0000001cH

; 419  : 
; 420  : 	// display header
; 421  : 	printf(_T("  Total  Kernel   User    PID   Process\n"));

	push	OFFSET FLAT:??_C@_0CJ@PJJL@?5?5Total?5?5Kernel?5?5?5User?5?5?5?5PID?5?5?5@ ; `string'
	call	_printf
	add	esp, 4

; 422  : 
; 423  : 
; 424  : 	// display most consuming processes
; 425  : 	for (ULONG i = 0; i < nCount; i++)

	mov	DWORD PTR _i$[ebp], 0
	jmp	SHORT $L48791
$L48792:
	mov	edx, DWORD PTR _i$[ebp]
	add	edx, 1
	mov	DWORD PTR _i$[ebp], edx
$L48791:
	mov	eax, DWORD PTR _i$[ebp]
	cmp	eax, DWORD PTR _nCount$[ebp]
	jae	$L48793

; 427  : 		p = rgTop[i];

	mov	ecx, DWORD PTR _i$[ebp]
	mov	edx, DWORD PTR _rgTop$[ebp+ecx*4]
	mov	DWORD PTR _p$[ebp], edx

; 428  : 		_ASSERTE(p != NULL);
; 429  : 
; 430  : 		nTotal = (LONG)((p->dwlKernelTime + p->dwlUserTime) * 10000 / dwlTotal);

	mov	eax, DWORD PTR _p$[ebp]
	mov	ecx, DWORD PTR _p$[ebp]
	mov	edx, DWORD PTR [eax+8]
	add	edx, DWORD PTR [ecx+16]
	mov	eax, DWORD PTR [eax+12]
	adc	eax, DWORD PTR [ecx+20]
	push	0
	push	10000					; 00002710H
	push	eax
	push	edx
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nTotal$[ebp], eax

; 431  : 		nKernel = (LONG)(p->dwlKernelTime * 10000 / dwlTotal);

	mov	edx, DWORD PTR _p$[ebp]
	push	0
	push	10000					; 00002710H
	mov	eax, DWORD PTR [edx+12]
	push	eax
	mov	ecx, DWORD PTR [edx+8]
	push	ecx
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nKernel$[ebp], eax

; 432  : 		nUser = (LONG)(p->dwlUserTime * 10000 / dwlTotal);

	mov	edx, DWORD PTR _p$[ebp]
	push	0
	push	10000					; 00002710H
	mov	eax, DWORD PTR [edx+20]
	push	eax
	mov	ecx, DWORD PTR [edx+16]
	push	ecx
	call	__allmul
	mov	ecx, DWORD PTR _dwlTotal$[ebp+4]
	push	ecx
	mov	ecx, DWORD PTR _dwlTotal$[ebp]
	push	ecx
	push	edx
	push	eax
	call	__aulldiv
	mov	DWORD PTR _nUser$[ebp], eax

; 433  : 		
; 434  : 
; 435  : 		printf(_T("%3d.%02d%% %3d.%02d%% %3d.%02d%% %5d %ls\n"),
; 436  : 				 nTotal / 100, nTotal % 100,
; 437  : 				 nKernel / 100, nKernel % 100,
; 438  : 				 nUser / 100, nUser % 100,
; 439  : 				 p->dwProcessId,
; 440  : 				 p->szPathName);

	mov	edx, DWORD PTR _p$[ebp]
	add	edx, 28					; 0000001cH
	push	edx
	mov	eax, DWORD PTR _p$[ebp]
	mov	ecx, DWORD PTR [eax+24]
	push	ecx
	mov	eax, DWORD PTR _nUser$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nUser$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	mov	eax, DWORD PTR _nKernel$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nKernel$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	mov	eax, DWORD PTR _nTotal$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	edx
	mov	eax, DWORD PTR _nTotal$[ebp]
	cdq
	mov	ecx, 100				; 00000064H
	idiv	ecx
	push	eax
	push	OFFSET FLAT:??_C@_0CK@KBOP@?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF?5?$CF3d?4?$CF02d?$CF?$CF@ ; `string'
	call	_printf
	add	esp, 36					; 00000024H

; 441  : 	}

	jmp	$L48792
$L48793:

; 442  : }

	mov	esp, ebp
	pop	ebp
	ret	0
?DisplayTop@@YAXPAU_PROCINFO@@0@Z ENDP			; DisplayTop
_TEXT	ENDS
PUBLIC	_main
PUBLIC	??_C@_0CG@JJCH@CoInitialize?5failed?5with?5code?50x@ ; `string'
PUBLIC	??_C@_0CO@KLPG@CoInitializeSecurity?5failed?5with@ ; `string'
PUBLIC	??_C@_0CK@JBDG@Failed?5to?5connect?5to?5WMI?5server?5@ ; `string'
EXTRN	__imp__Sleep@4:NEAR
EXTRN	__imp__CoInitialize@4:NEAR
EXTRN	__imp__CoUninitialize@0:NEAR
EXTRN	__imp__CloseHandle@4:NEAR
EXTRN	__imp__CoInitializeSecurity@36:NEAR
EXTRN	__imp__GetVersionExA@4:NEAR
;	COMDAT ??_C@_0CG@JJCH@CoInitialize?5failed?5with?5code?50x@
; File J:\jacob.top\wmitop.cpp
CONST	SEGMENT
??_C@_0CG@JJCH@CoInitialize?5failed?5with?5code?50x@ DB 'CoInitialize fai'
	DB	'led with code 0x%08X', 0aH, 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_0CO@KLPG@CoInitializeSecurity?5failed?5with@
CONST	SEGMENT
??_C@_0CO@KLPG@CoInitializeSecurity?5failed?5with@ DB 'CoInitializeSecuri'
	DB	'ty failed with code 0x%08X', 0aH, 00H	; `string'
CONST	ENDS
;	COMDAT ??_C@_0CK@JBDG@Failed?5to?5connect?5to?5WMI?5server?5@
CONST	SEGMENT
??_C@_0CK@JBDG@Failed?5to?5connect?5to?5WMI?5server?5@ DB 'Failed to conn'
	DB	'ect to WMI server (0x%08X)', 0aH, 00H	; `string'
CONST	ENDS
_TEXT	SEGMENT
_pCurr$ = -4
_pPrev$ = -16
_hRes$ = -8
_pServer$ = -12
_main	PROC NEAR

; 457  : {

	push	ebp
	mov	ebp, esp
	sub	esp, 16					; 00000010H

; 458  : 	PPROCINFO pCurr = NULL;

	mov	DWORD PTR _pCurr$[ebp], 0

; 459  : 	PPROCINFO pPrev = NULL;

	mov	DWORD PTR _pPrev$[ebp], 0

; 460  : 	HRESULT hRes;
; 461  : 
; 462  : 	IWbemServices * pServer = NULL;

	mov	DWORD PTR _pServer$[ebp], 0

; 463  : 
; 464  : 	_osvi.dwOSVersionInfoSize = sizeof(_osvi);

	mov	DWORD PTR ?_osvi@@3U_OSVERSIONINFOA@@A, 148 ; 00000094H

; 465  : 	_VERIFY(GetVersionEx(&_osvi));

	push	OFFSET FLAT:?_osvi@@3U_OSVERSIONINFOA@@A ; _osvi
	call	DWORD PTR __imp__GetVersionExA@4

; 466  : 
; 467  : 	// initialize COM library
; 468  : 	hRes = CoInitialize(NULL);

	push	0
	call	DWORD PTR __imp__CoInitialize@4
	mov	DWORD PTR _hRes$[ebp], eax

; 469  : 	if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48807

; 471  : 		printf(_T("CoInitialize failed with code 0x%08X\n"), hRes);

	mov	eax, DWORD PTR _hRes$[ebp]
	push	eax
	push	OFFSET FLAT:??_C@_0CG@JJCH@CoInitialize?5failed?5with?5code?50x@ ; `string'
	call	_printf
	add	esp, 8

; 472  : 		return -1;

	or	eax, -1
	jmp	$L48800
$L48807:

; 477  : 			  NULL,
; 478  : 			  -1,
; 479  : 			  NULL,
; 480  : 			  NULL,
; 481  : 			  RPC_C_AUTHN_LEVEL_CONNECT,
; 482  : 			  RPC_C_IMP_LEVEL_IMPERSONATE,
; 483  : 			  NULL,
; 484  : 			  EOAC_NONE,
; 485  : 			  0);

	push	0
	push	0
	push	0
	push	3
	push	2
	push	0
	push	0
	push	-1
	push	0
	call	DWORD PTR __imp__CoInitializeSecurity@36
	mov	DWORD PTR _hRes$[ebp], eax

; 486  : 	if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48810

; 488  : 		printf(_T("CoInitializeSecurity failed with code 0x%08X\n"), hRes);

	mov	ecx, DWORD PTR _hRes$[ebp]
	push	ecx
	push	OFFSET FLAT:??_C@_0CO@KLPG@CoInitializeSecurity?5failed?5with@ ; `string'
	call	_printf
	add	esp, 8

; 489  : 		CoUninitialize();

	call	DWORD PTR __imp__CoUninitialize@0

; 490  : 		return -1;

	or	eax, -1
	jmp	$L48800
$L48810:

; 492  : 
; 493  : 	// connect with the WMI server
; 494  : 	hRes = DoConnect(NULL, &pServer);

	lea	edx, DWORD PTR _pServer$[ebp]
	push	edx
	push	0
	call	?DoConnect@@YAJPBDPAPAUIWbemServices@@@Z ; DoConnect
	add	esp, 8
	mov	DWORD PTR _hRes$[ebp], eax

; 495  : 	if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48813

; 497  : 		printf(_T("Failed to connect to WMI server (0x%08X)\n"), hRes);

	mov	eax, DWORD PTR _hRes$[ebp]
	push	eax
	push	OFFSET FLAT:??_C@_0CK@JBDG@Failed?5to?5connect?5to?5WMI?5server?5@ ; `string'
	call	_printf
	add	esp, 8

; 498  : 		CoUninitialize();

	call	DWORD PTR __imp__CoUninitialize@0

; 499  : 		return -1;

	or	eax, -1
	jmp	$L48800
$L48813:

; 501  : 
; 502  : 
; 503  : 	// obtain initial statistics
; 504  : 	hRes = GetStatistics(pServer, &pCurr);

	lea	ecx, DWORD PTR _pCurr$[ebp]
	push	ecx
	mov	edx, DWORD PTR _pServer$[ebp]
	push	edx
	call	?GetStatistics@@YAJPAUIWbemServices@@PAPAU_PROCINFO@@@Z ; GetStatistics
	add	esp, 8
	mov	DWORD PTR _hRes$[ebp], eax

; 505  : 	if (FAILED(hRes))

	cmp	DWORD PTR _hRes$[ebp], 0
	jge	SHORT $L48816

; 507  : 		pServer->Release();

	mov	eax, DWORD PTR _pServer$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	edx, DWORD PTR _pServer$[ebp]
	push	edx
	call	DWORD PTR [ecx+8]

; 508  : 		CoUninitialize();

	call	DWORD PTR __imp__CoUninitialize@0

; 509  : 		return -1;

	or	eax, -1
	jmp	$L48800
$L48816:

; 511  : 
; 512  : 		if (pPrev != NULL)

	cmp	DWORD PTR _pPrev$[ebp], 0
	je	SHORT $L48817

; 513  : 			FreeProcInfoList(pPrev);

	mov	eax, DWORD PTR _pPrev$[ebp]
	push	eax
	call	?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z	; FreeProcInfoList
	add	esp, 4
$L48817:

; 514  : 		pPrev = pCurr;

	mov	ecx, DWORD PTR _pCurr$[ebp]
	mov	DWORD PTR _pPrev$[ebp], ecx

; 515  : 
; 516  :       // Das System muss sich erstmal beruhigen. Das laden des Programms 'top.exe' hat die CPU belastet( DLL, Lib init....)
; 517  :       //
; 518  :       Sleep(1000);

	push	1000					; 000003e8H
	call	DWORD PTR __imp__Sleep@4

; 519  : 		hRes = GetStatistics(pServer, &pCurr);

	lea	edx, DWORD PTR _pCurr$[ebp]
	push	edx
	mov	eax, DWORD PTR _pServer$[ebp]
	push	eax
	call	?GetStatistics@@YAJPAUIWbemServices@@PAPAU_PROCINFO@@@Z ; GetStatistics
	add	esp, 8
	mov	DWORD PTR _hRes$[ebp], eax

; 520  : 
; 521  : 		// display the difference
; 522  : 		DisplayTop(pCurr, pPrev);

	mov	ecx, DWORD PTR _pPrev$[ebp]
	push	ecx
	mov	edx, DWORD PTR _pCurr$[ebp]
	push	edx
	call	?DisplayTop@@YAXPAU_PROCINFO@@0@Z	; DisplayTop
	add	esp, 8

; 523  : 
; 524  : 	pServer->Release();

	mov	eax, DWORD PTR _pServer$[ebp]
	mov	ecx, DWORD PTR [eax]
	mov	edx, DWORD PTR _pServer$[ebp]
	push	edx
	call	DWORD PTR [ecx+8]

; 525  : 
; 526  : 	if (pPrev != NULL)

	cmp	DWORD PTR _pPrev$[ebp], 0
	je	SHORT $L48818

; 527  : 		FreeProcInfoList(pPrev);

	mov	eax, DWORD PTR _pPrev$[ebp]
	push	eax
	call	?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z	; FreeProcInfoList
	add	esp, 4
$L48818:

; 528  : 	if (pCurr != NULL)

	cmp	DWORD PTR _pCurr$[ebp], 0
	je	SHORT $L48819

; 529  : 		FreeProcInfoList(pCurr);

	mov	ecx, DWORD PTR _pCurr$[ebp]
	push	ecx
	call	?FreeProcInfoList@@YAXPAU_PROCINFO@@@Z	; FreeProcInfoList
	add	esp, 4
$L48819:

; 530  : 
; 531  : 	CloseHandle(_hStop);

	mov	edx, DWORD PTR ?_hStop@@3PAXA		; _hStop
	push	edx
	call	DWORD PTR __imp__CloseHandle@4

; 532  : 
; 533  : 	CoUninitialize();

	call	DWORD PTR __imp__CoUninitialize@0

; 534  : 	return 0;

	xor	eax, eax
$L48800:

; 535  : }

	mov	esp, ebp
	pop	ebp
	ret	0
_main	ENDP
_TEXT	ENDS
END
