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
#include "pch.h"
#include <jni.h>
#include "JCConnection.h"
#include "jshell_utils.h"
#include "com_simplifiedlogic_nitro_jlink_cpp_JCConnection.h"

#include <cipxx.h>
#include <ciplib.h>
#include <pfcGlobal.h>
#include <pfcAsyncConnection.h>

pfcAsyncConnection_ptr JCConnection::savedConn = nullptr;

static bool connDebug = false;

/*
* Notes on how connections work in jshell:
* each API calls JLConnectionUtil.getJLSession(sess.getConnectionId())
*	JLISession.getConnectionId() 
*		checks whether connId==null
*		if connId==null,
*			setConnection()
*				JLConnectionUtil.makeAsyncConnection()
*					looks for existing entry in connections
*					if none,
*						createConnectionEntry(null)
*							JLGlobal.loadLibrary();
*							pfcAsyncConnection.connect()
*							getNewConnId(async) to get new connectionid's external rep from async
*							adds conn to connections
*	JLConnectionUtil.getJLSession(connId)
*		getConnection(connId) to get conn from connections
*		if none found, return null
*		test connection with sess.getCurrentDirectory()
*		if bad, 
*			createConnectionEntry(connId)
*/
#ifdef __cplusplus
extern "C" {
#endif
	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCConnection_setConnectionDebug
	(JNIEnv*, jclass, jboolean debug)
	{
		connDebug = (bool)debug;
	}

	JNIEXPORT jstring JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCConnection_connect
	(JNIEnv* env, jclass)
	{
		if (connDebug) puts("Connect()");
		/*
		//jclass exClass = env->FindClass("java/lang/Exception");
		
		JCConnection connHandler;
		try {
			xstring newid = connHandler.connect();

			// convert xstring back to jstring
			return env->NewStringUTF(newid);
		}
		xcatchbegin
			xcatch(cipXException, ciperr)
			{
				handleCipXException(env, ciperr);
			}
			xcatch(pfcXToolkitError, e)
			{
				handlePfcXToolkitError(env, e);
			}
			xcatch(pfcXPFC, xpfcerr)
			{
				handlePfcXPFC(env, xpfcerr);
			}
		xcatchend
		catch (...) {
			cerr << "RAISING SIGNAL : Signal number :%d Abnormal Program Termination." << endl;
		}
		*/
		return nullptr;
	}

	JNIEXPORT jstring JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCConnection_connectById
	(JNIEnv* env, jclass, jstring connIdIn)
	{
		if (connDebug) puts("ConnectById(connid)");
		/*
		cout << connIdIn << "\n";
		jclass exClass = env->FindClass("java/lang/Exception");

		JCConnection connHandler;
		try {
			if (connIdIn == NULL) {
				xstring newid = connHandler.connect();

				// convert xstring back to jstring
				return env->NewStringUTF(newid);
			}
			else {
				// convert jstring to xstring
				const char* connidStr = (*env).GetStringUTFChars(connIdIn, 0);
				xstring connId = connidStr;
				env->ReleaseStringUTFChars(connIdIn, connidStr);

				xstring newid = connHandler.connect(connId);

				// convert xstring back to jstring
				return env->NewStringUTF(newid);
			}
			return nullptr;
		}
		xcatchbegin
			xcatch(cipXException, ciperr)
			{
				handleCipXException(env, ciperr);
			}
			xcatch(pfcXToolkitError, e)
			{
				handlePfcXToolkitError(env, e);
			}
			xcatch(pfcXPFC, xpfcerr)
			{
				handlePfcXPFC(env, xpfcerr);
			}
		xcatchend
		catch (...) {
			cerr << "RAISING SIGNAL : Signal number :%d Abnormal Program Termination." << endl;
		}
		*/
		return nullptr;
	}

#ifdef __cplusplus
}
#endif

bool JCConnection::isDebug() {
	return connDebug;
}

xstring JCConnection::connect() {
/*
	xstring connId;
	//connId = makeAsyncConnection();
	if (async != nullptr) {
		// get connid from async
		try {
			async->GetSession()->GetCurrentDirectory();
		}
		catch (...) {
			async->Disconnect(2);
			async = nullptr;
		}
	}
	if (async == nullptr) {
		connId = makeAsyncConnection();
	}
	return connId;
*/
	return xstringnil;
}

xstring JCConnection::connect(xstring connId) {
/*
	if (connId.IsNull()) {
		//connId = makeAsyncConnection();
		return connect();
	}
	else {
		if (async != nullptr) {
			// get connid from async
			pfcConnectionId_ptr connptr = async->GetConnectionId();
			if (connptr->GetExternalRep() != connId) {
				// if doesn't match, then disconnect and null async
				async->Disconnect(2);
				async = nullptr;
			}
			else {
				try {
					async->GetSession()->GetCurrentDirectory();
				}
				catch (...) {
					async->Disconnect(2);
					async = nullptr;
				}
			}
		}
		if (async == nullptr)
			connId = makeAsyncConnection(connId);
	}
	return connId;
*/
	return xstringnil;
}

xstring JCConnection::makeAsyncConnection(xstring connId) {
	if (connDebug) cout << "makeAsyncConnection(): " << connId << endl;
	// TODO: check dict instead
	if (savedConn != nullptr)
		return getNewConnId(savedConn);
	try {
		connId = createConnectionEntry(xstringnil);
	}
	xcatchbegin
		xcatch(pfcXToolkitAmbiguous, e)
		{
			throw std::exception("Unable to connect to Creo; more than one instance of Creo is running");
		}
	xcatchend
	catch (...) {
		throw std::exception("Unable to connect to Creo through OTK");
	}
	return connId;
}

xstring JCConnection::createConnectionEntry(xstring connId) {
	if (connDebug) cout << "createConnectionEntry " << connId << endl;
	pfcAsyncConnection_ptr aconn;
	if (connId.IsNull()) {
		aconn = pfcAsyncConnection::Connect(NULL, NULL, NULL, 10);
		connId = getNewConnId(aconn);
	}
	else {
		pfcConnectionId_ptr connptr = pfcConnectionId::Create(connId);
		aconn = pfcAsyncConnection::ConnectById(connptr, NULL, 10);
	}
	saveConnection(aconn);
	return connId;
}

void JCConnection::closeAsyncConnection(xstring connId) {
	if (connDebug) cout << "closeAsyncConnection " << connId << endl;
	if (connId.IsNull())
		return;
	// not doing the rest because this is single-connect
}

xstring JCConnection::getNewConnId(pfcAsyncConnection_ptr aconn) {
	if (connDebug) puts("getNewConnId");
	try {
		pfcConnectionId_ptr connptr = aconn->GetConnectionId();
		return connptr->GetExternalRep();
	}
	catch (...) {
		throw std::exception("Could not get Creo connection ID");
	}
}

void JCConnection::saveConnection(pfcAsyncConnection_ptr aconn) {
	if (connDebug) puts("saveConnection");
	// TODO: store in a dict
	savedConn = aconn;
}

pfcAsyncConnection_ptr JCConnection::getConnection(xstring connId) {
	if (connDebug) puts("getConnection");
	// TODO: get from a dict
	return savedConn;
}

void JCConnection::deleteConnection(xstring connId) {
	if (connDebug) puts("deleteConnection");
	// TODO: delete from a dict
	if (savedConn != nullptr) {
		savedConn->Disconnect(2);
		savedConn = nullptr;
	}
}

pfcSession_ptr JCConnection::getSession(xstring connId) {
	if (connDebug) cout << "getSession " << connId << endl;
	pfcAsyncConnection_ptr aconn = getConnection(connId);
	if (aconn == nullptr)
		return nullptr;
	else {
		pfcSession_ptr sess = nullptr;
		try {
			sess = aconn->GetSession();
		}
		catch (...) {
			throw std::exception("Could not get Creo Session ID");
		}

		// get connid from async
		pfcConnectionId_ptr connptr = aconn->GetConnectionId();
		xstring oldid = connptr->GetExternalRep();
		if (oldid != connId) {
			// if doesn't match, then disconnect and null async
			deleteConnection(oldid);
			aconn = nullptr;
		}
		else {
			try {
				aconn->GetSession()->GetCurrentDirectory();
			}
			catch (...) {
				deleteConnection(oldid);
				aconn = nullptr;
			}
		}
		if (aconn == nullptr) {
			createConnectionEntry(connId);
			aconn = getConnection(connId);
			sess = aconn->GetSession();
		}
		return sess;
	}
}

