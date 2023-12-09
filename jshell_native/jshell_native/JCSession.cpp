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
#include "JCSession.h"
#include "JCConnection.h"
#include "com_simplifiedlogic_nitro_jlink_cpp_JCConnection.h"

//std::unordered_map<xstring, JCSession> JCSession::sessionMap;
std::vector<JCSession> JCSession::sessionList;

static bool sessDebug = false;

JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCConnection_setSessionDebug
(JNIEnv*, jclass, jboolean debug)
{
	sessDebug = (bool)debug;
}

JCSession::JCSession() {
	if (sessDebug) puts("JCSession()");
	sessionId = xstringnil;
	connId = xstringnil;
}

JCSession::JCSession(xstring sessionIdIn) {
	if (sessDebug) puts("JCSession(sessionId)");
	setSessionId(sessionIdIn);
	connId = xstringnil;
}

bool JCSession::isNull() {
	return sessionId.IsNull();
}

JCSession* JCSession::createSession(xstring sessionId) {
	if (sessDebug) puts("createSession");
	JCSession sess(sessionId);
	deleteSession(sessionId); // in case one already exists for this session
	sessionList.push_back(sess);
	return &sessionList.back();
}

JCSession* JCSession::getSession(xstring sessionId) {
	if (sessDebug) puts("getSession");
	if (sessionId.IsNull())
		return nullptr;
//	if (sessionMap.count(sessionId) == 0)
//		return nullptr;
//	JCSession sess = sessionMap[sessionId];
// set last used?
//	return &sess;
	size_t len = sessionList.size();
	if (len == 0)
		return nullptr;
	for (int i = 0; i < len; i++) {
		JCSession sess = sessionList[i];
		if (sess.getSessionId() == sessionId)
			return &sessionList[i];
	}
// set last used?

	return nullptr;
}

void JCSession::deleteSession(xstring sessionId) {
	if (sessDebug) puts("deleteSession");
	if (sessionId.IsNull())
		return;
//	if (sessionMap.count(sessionId) == 0)
//		return;
//	JCSession sess = sessionMap[sessionId];
//	sessionMap.erase(sessionId);
//	sess.cleanSession();
//	sess.setSessionId(xstringnil);
	size_t len = sessionList.size();
	if (len == 0)
		return;
	for (int i = 0; i < len; i++) {
		JCSession sess = sessionList[i];
		if (sess.getSessionId() == sessionId) {
			sess.cleanSession();
			sess.setSessionId(xstringnil);
			sessionList.erase(sessionList.begin()+i);
			len--;
			i--;
		}
	}
}

void JCSession::cleanSession() {
	if (sessDebug) puts("cleanSession");
	if (!connId.IsNull()) {
		JCConnection connHandler;
		connHandler.closeAsyncConnection(connId);
		connId = xstringnil;
	}
}

void JCSession::setConnection() {
	if (sessDebug) puts("setConnection");
	JCConnection connHandler;
	connId = connHandler.makeAsyncConnection(xstringnil);
}

xstring JCSession::getConnectionId() {
	if (sessDebug) cout << "getConnectionId, connId=" << connId << endl;
	if (connId.IsNull())
		setConnection();
	return connId;
}

void JCSession::setConnectionId(xstring connIdIn) {
	if (sessDebug) puts("setConnectionId");
	connId = connIdIn;
}

xstring JCSession::getSessionId() {
	return sessionId;
}

void JCSession::setSessionId(xstring sessionIdIn) {
	sessionId = sessionIdIn;
}
