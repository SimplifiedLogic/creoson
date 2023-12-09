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
#include "JCFamilyTable.h"

#include "JCConnection.h"
#include "JCSession.h"
#include "jshell_utils.h"
#include "com_simplifiedlogic_nitro_jlink_cpp_JCFamilyTable.h"

bool famtableDebug = false;

#ifdef __cplusplus
extern "C" {
#endif

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFamilyTable_setDebug
	(JNIEnv*, jobject, jboolean debug)
	{
		famtableDebug = (bool)debug;
	}

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFamilyTable_replaceComponent
	(JNIEnv* env, jobject, jstring sessionIdIn, jstring assemblyNameIn, jintArray compIds, jstring newSolidNameIn)
	{
		if (famtableDebug) puts("FamilyTable replaceComponent:");

		const jsize len = env->GetArrayLength(compIds);
		printf("Comp IDS (%d)\n", len);
		jboolean isCopy;
		std::vector<int> ids;
		jint* body = env->GetIntArrayElements(compIds, &isCopy);
		for (int i = 0; i < len; i++) {
			//printf("\t%d : %d\n", i, body[i]);
			ids.push_back(body[i]);
		}

		const char* sessionIdStr = (*env).GetStringUTFChars(sessionIdIn, 0);
		xstring sessionId = sessionIdStr;
		env->ReleaseStringUTFChars(sessionIdIn, sessionIdStr);
		if (famtableDebug) cout << "sessionId: " << sessionId << endl;

		xstring assemblyName = xstringnil;
		if (assemblyNameIn != nullptr) {
			const char* assemblyNameStr = (*env).GetStringUTFChars(assemblyNameIn, 0);
			assemblyName = assemblyNameStr;
			env->ReleaseStringUTFChars(assemblyNameIn, assemblyNameStr);
		}
		if (famtableDebug) cout << "assemblyName: " << assemblyName << endl;

		xstring newSolidName = xstringnil;
		if (newSolidNameIn != nullptr) {
			const char* newSolidNameStr = (*env).GetStringUTFChars(newSolidNameIn, 0);
			newSolidName = newSolidNameStr;
			env->ReleaseStringUTFChars(newSolidNameIn, newSolidNameStr);
		}
		if (famtableDebug) cout << "newSolidName: " << newSolidName << endl;

		try {
			JCSession sessHandler;
			JCSession* jcsess_ptr = sessHandler.getSession(sessionId);
			if (jcsess_ptr == nullptr) {
				jcsess_ptr = sessHandler.createSession(sessionId);
			}
			if (famtableDebug) cout << "session ptr: " << jcsess_ptr << endl;

			JCFamilyTable famtableHandler;

			famtableHandler.replaceComponent(jcsess_ptr, assemblyName, ids, newSolidName);
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
			catch (const std::exception& ex) {
			handleException(env, ex);
		}
		catch (...) {
			cerr << "RAISING SIGNAL : Signal number :%d Abnormal Program Termination." << endl;
		}

		env->ReleaseIntArrayElements(compIds, body, JNI_ABORT);
	}

#ifdef __cplusplus
}
#endif

void JCFamilyTable::replaceComponent(JCSession* jcsess_ptr, xstring assemblyName, std::vector<int> compIds, xstring newSolidName) {
	JCConnection connHandler;

	if (famtableDebug) cout << "replaceComponent: " << jcsess_ptr << endl;

	xstring cid = jcsess_ptr->getConnectionId();
	pfcSession_ptr sess = connHandler.getSession(cid);
	if (sess == nullptr) {
		throw std::exception("Could not get Creo Session in native library!");
	}

	pfcModel_ptr model = nullptr;
	if (assemblyName.IsNull()) {
		model = sess->GetCurrentModel();
	}
	else {
		model = sess->GetModelFromFileName(assemblyName);
	}
	if (model == nullptr) {
		throw std::exception("Wrapping Asssembly is null or not found!");
	}
	if (!pfcAssembly::isObjKindOf(model)) {
		throw std::exception("Model is not an assembly!");
	}
	if (famtableDebug) cout << "Assembly: " << model->GetDescr()->GetFileName() << "\n";

	pfcModel_ptr newModel = nullptr;
	if (newSolidName.IsNull()) {
		throw std::exception("New Solid Name is null!");
	}
	newModel = sess->GetModelFromFileName(newSolidName);
	if (newModel == nullptr) {
		throw std::exception("New Solid is not found!");
	}
	if (!pfcSolid::isObjKindOf(newModel)) {
		throw std::exception("New Model is not a solid!");
	}
	if (famtableDebug) cout << "New Solid: " << newModel->GetDescr()->GetFileName() << "\n";


	pfcAssembly_ptr assembly = pfcAssembly::cast(model);
	wfcWAssembly_ptr wassembly = wfcWAssembly::cast(assembly);

	pfcSolid_ptr solid = pfcSolid::cast(newModel);
	wfcWSolid_ptr wsolid = wfcWSolid::cast(newModel);

	xintsequence_ptr comp_ids = xintsequence::create();
	size_t len = compIds.size();
	for (int i = 0; i < len; i++) {
		comp_ids->append(compIds[i]);
	}

	// use wfcWAssembly::AutoInterchange
	wassembly->AutoInterchange(comp_ids, wsolid);
}
