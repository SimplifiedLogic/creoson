#pragma once
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
#include <xstring.h>

#include <pfcAsyncConnection.h>
#include <wfcSession.h>

class JCConnection {
public:
	xstring connect();
	xstring connect(xstring connId);

	static pfcSession_ptr getSession(xstring connId);
	static void closeAsyncConnection(xstring connId);
	static xstring makeAsyncConnection(xstring connId);

	static bool isDebug();

private:
	static xstring createConnectionEntry(xstring connId);
	static xstring getNewConnId(pfcAsyncConnection_ptr conn);

	static void saveConnection(pfcAsyncConnection_ptr aconn);
	static pfcAsyncConnection_ptr getConnection(xstring connId);
	static void deleteConnection(xstring connId);

	static pfcAsyncConnection_ptr savedConn;

};
