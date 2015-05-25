// alexfstd.h
//
// $Id: alexfstd.h,v 1.1 2002/04/14 07:36:36 afedotov Exp $
//

#if _MSC_VER >= 1100
#   define __novtable	__declspec(novtable)
#else
#   define __novtable
#endif

#if _MSC_VER >= 1100 && defined(__cplusplus)
#   define __nothrow	__declspec(nothrow)
#else
#   define __nothrow
#endif

#if _MSC_VER >= 1000
#   define __import	__declspec(dllimport)
#   define __export	__declspec(dllexport)
#endif

#if _MSC_VER < 1200
#   define __assume(x)
#endif

#ifdef _MSC_FULL_VER 
#   define __align(x)	__declspec(align(x))
#elif !defined(__align)
#   define __align(x)
#endif

#if _MSC_VER >= 1200
#	define __noreturn __declspec(noreturn)
#else
#	define __noreturn
#endif

#pragma warning(disable: 4646)	// function declared with noreturn has 
								// non-void return type

#if _MSC_VER < 1210
#	define __noop	((void)0)
#endif

#ifndef countof
#   define countof(a)	    (sizeof(a)/sizeof(a[0]))
#endif

#ifndef offsetof
#   define offsetof(a, b)   ((size_t)&(((a *)0)->b))
#endif

#ifndef _VERIFY
#   ifdef _DEBUG
#		define _VERIFY(x)   _ASSERTE(x)
#   else
#		define _VERIFY(x)   ((void)(x))
#   endif
#endif

#ifndef _DEBUG_ONLY
#   ifdef _DEBUG
#		define _DEBUG_ONLY(x)	((void)(x))
#   else
#		define _DEBUG_ONLY(x)	__noop
#   endif
#endif

#ifndef _UNUSED
#   if _MSC_VER >= 1000
#		define _UNUSED(x)	x
#   else
#		define _UNUSED(x)	x = x
#   endif
#endif

#ifdef _DEBUG
#	define _RPT_API_FAILED(name)	\
	    do { unsigned __err = GetLastError(); \
		 _RPTF1(_CRT_WARN, #name "() failed; ERR=%d\n", __err); \
		 SetLastError(__err); } while(0)
#else
#	define _RPT_API_FAILED(name)	__noop
#endif
