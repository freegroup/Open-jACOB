// stdafx.h
//
// $Id: stdafx.h,v 1.2 2003/08/09 06:33:21 afedotov Exp $
//

#define STRICT
#define _CRTDBG_MAP_ALLOC
#define _WIN32_WINNT		0x0400
#define _WIN32_IE			0x0400
#define INLINE_HRESULT_FROM_WIN32

#include <windows.h>
#include <windowsx.h>
#include <tchar.h>
#include <wbemidl.h>

#include <limits.h>
#include <malloc.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <crtdbg.h>

#pragma intrinsic(memset, memcmp, memcpy, abs)

#include "alexfstd.h"
