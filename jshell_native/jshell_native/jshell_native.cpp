/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS",
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
// jshell_native.cpp : Defines the exported functions for the DLL.
//

#include "pch.h"
#include "framework.h"
#include "jshell_native.h"
#include "JCConnection.h"


// This is an example of an exported variable
//JSHELLNATIVE_API int njshellnative=0;

/*
// This is an example of an exported function.
JSHELLNATIVE_API int fnjshellnative(void)
{
    return 0;
}

// This is the constructor of a class that has been exported.
Cjshellnative::Cjshellnative()
{
    return;
}
*/

JSHELLNATIVE_API xstring test_connect() {
    JCConnection connHandler;
    return connHandler.connect();
}

JSHELLNATIVE_API xstring test_connect(xstring connId) {
    JCConnection connHandler;
    return connHandler.connect(connId);
}
