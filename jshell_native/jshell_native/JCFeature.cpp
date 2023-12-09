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
#include <vector>
#include <jni.h>
#include "JCFeature.h"
#include "JCConnection.h"
#include "JCSession.h"
#include "jshell_utils.h"
#include "com_simplifiedlogic_nitro_jlink_cpp_JCFeature.h"

#include <wfcSession.h>

bool featureDebug = false;

#ifdef __cplusplus
extern "C" {
#endif
	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFeature_setDebug
	(JNIEnv*, jobject, jboolean debug)
	{
		featureDebug = (bool)debug;
	}

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFeature_featuresSuppress
	(JNIEnv* env, jobject, jstring sessionIdIn, jstring modelNameIn, jintArray featIds, jboolean clip)
	{
		if (featureDebug) puts("Feature Suppress:");
		// https://web.mit.edu/javadev/doc/tutorial/native1.1/implementing/array.html
		const jsize len = env->GetArrayLength(featIds);
		//printf("Feat IDS (%d)\n", len);
		jboolean isCopy;
		std::vector<int> ids;
		jint* body = env->GetIntArrayElements(featIds, &isCopy);
		for (int i = 0; i < len; i++) {
			//printf("\t%d : %d\n", i, body[i]);
			ids.push_back(body[i]);
		}
		//env->GetIntArrayRegion(featIds, 0, len, &ids[0]);

		const char* sessionIdStr = (*env).GetStringUTFChars(sessionIdIn, 0);
		xstring sessionId = sessionIdStr;
		env->ReleaseStringUTFChars(sessionIdIn, sessionIdStr);
		if (featureDebug) cout << "sessionId: " << sessionId << endl;

		xstring modelName = xstringnil;
		if (modelNameIn != nullptr) {
			const char* modelNameStr = (*env).GetStringUTFChars(modelNameIn, 0);
			modelName = modelNameStr;
			env->ReleaseStringUTFChars(modelNameIn, modelNameStr);
		}
		if (featureDebug) cout << "modelName: " << modelName << endl;

		try {
			JCSession sessHandler;
			JCSession* jcsess_ptr = sessHandler.getSession(sessionId);
			if (jcsess_ptr == nullptr) {
				jcsess_ptr = sessHandler.createSession(sessionId);
			}
			if (featureDebug) cout << "session ptr: " << jcsess_ptr << endl;

			JCFeature featHandler;

			featHandler.suppress(jcsess_ptr, modelName, ids, (bool)clip);
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

		// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/functions.html look up ReleaseIntArrayElements for args
		env->ReleaseIntArrayElements(featIds, body, JNI_ABORT);
	}

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFeature_featuresResume
	(JNIEnv* env, jobject, jstring sessionIdIn, jstring modelNameIn, jintArray featIds)
	{
		if (featureDebug) puts("Feature Resume:");
		// https://web.mit.edu/javadev/doc/tutorial/native1.1/implementing/array.html
		const jsize len = env->GetArrayLength(featIds);
		//printf("Feat IDS (%d)\n", len);
		jboolean isCopy;
		std::vector<int> ids;
		jint* body = env->GetIntArrayElements(featIds, &isCopy);
		for (int i = 0; i < len; i++) {
			//printf("\t%d : %d\n", i, body[i]);
			ids.push_back(body[i]);
		}
		//env->GetIntArrayRegion(featIds, 0, len, &ids[0]);

		const char* sessionIdStr = (*env).GetStringUTFChars(sessionIdIn, 0);
		xstring sessionId = sessionIdStr;
		env->ReleaseStringUTFChars(sessionIdIn, sessionIdStr);
		if (featureDebug) cout << "sessionId: " << sessionId << endl;

		xstring modelName = xstringnil;
		if (modelNameIn != nullptr) {
			const char* modelNameStr = (*env).GetStringUTFChars(modelNameIn, 0);
			modelName = modelNameStr;
			env->ReleaseStringUTFChars(modelNameIn, modelNameStr);
		}
		if (featureDebug) cout << "modelName: " << modelName << endl;

		try {
			JCSession sessHandler;
			JCSession* jcsess_ptr = sessHandler.getSession(sessionId);
			if (jcsess_ptr == nullptr) {
				jcsess_ptr = sessHandler.createSession(sessionId);
			}
			if (featureDebug) cout << "session ptr: " << jcsess_ptr << endl;

			JCFeature featHandler;

			featHandler.resume(jcsess_ptr, modelName, ids);
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

		// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/functions.html look up ReleaseIntArrayElements for args
		env->ReleaseIntArrayElements(featIds, body, JNI_ABORT);
	}

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFeature_featuresDelete
	(JNIEnv* env, jobject, jstring sessionIdIn, jstring modelNameIn, jintArray featIds, jboolean clip)
	{
		if (featureDebug) puts("Feature Delete:");
		// https://web.mit.edu/javadev/doc/tutorial/native1.1/implementing/array.html
		const jsize len = env->GetArrayLength(featIds);
		//printf("Feat IDS (%d)\n", len);
		jboolean isCopy;
		std::vector<int> ids;
		jint* body = env->GetIntArrayElements(featIds, &isCopy);
		for (int i = 0; i < len; i++) {
			//printf("\t%d : %d\n", i, body[i]);
			ids.push_back(body[i]);
		}
		//env->GetIntArrayRegion(featIds, 0, len, &ids[0]);

		const char* sessionIdStr = (*env).GetStringUTFChars(sessionIdIn, 0);
		xstring sessionId = sessionIdStr;
		env->ReleaseStringUTFChars(sessionIdIn, sessionIdStr);
		if (featureDebug) cout << "sessionId: " << sessionId << endl;

		xstring modelName = xstringnil;
		if (modelNameIn != nullptr) {
			const char* modelNameStr = (*env).GetStringUTFChars(modelNameIn, 0);
			modelName = modelNameStr;
			env->ReleaseStringUTFChars(modelNameIn, modelNameStr);
		}
		if (featureDebug) cout << "modelName: " << modelName << endl;

		try {
			JCSession sessHandler;
			JCSession* jcsess_ptr = sessHandler.getSession(sessionId);
			if (jcsess_ptr == nullptr) {
				jcsess_ptr = sessHandler.createSession(sessionId);
			}
			if (featureDebug) cout << "session ptr: " << jcsess_ptr << endl;

			JCFeature featHandler;

			featHandler.deleteFeature(jcsess_ptr, modelName, ids, (bool)clip);
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

		// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/functions.html look up ReleaseIntArrayElements for args
		env->ReleaseIntArrayElements(featIds, body, JNI_ABORT);
	}

#ifdef __cplusplus
}
#endif

void JCFeature::suppress(JCSession* jcsess_ptr, xstring modelName, std::vector<int> featIds, bool clip) {
	JCConnection connHandler;

	if (featureDebug) cout << "suppress: " << jcsess_ptr << endl;

	xstring cid = jcsess_ptr->getConnectionId();
	pfcSession_ptr sess = connHandler.getSession(cid);
	if (sess == nullptr) {
		throw std::exception("Could not get Creo Session in native library!");
	}

	pfcModel_ptr model = nullptr;
	if (modelName.IsNull()) {
		model = sess->GetCurrentModel();
	}
	else {
		model = sess->GetModelFromFileName(modelName);
	}

	if (model == nullptr) {
		throw std::exception("Model is null or not found!");
	}
	if (!pfcSolid::isObjKindOf(model)) {
		throw std::exception("Model is not a solid!");
	}
	if (featureDebug) cout << "Model: " << model->GetDescr()->GetFileName() << "\n";

	pfcSolid_ptr solid = pfcSolid::cast(model);
	//pfcFeature_ptr feature = solid->GetFeatureById(featureId);
	wfcWSolid_ptr wsolid = wfcWSolid::cast(solid);

	xintsequence_ptr feat_ids = xintsequence::create();
	size_t len = featIds.size();
	for (int i = 0; i < len; i++) {
		feat_ids->append(featIds[i]);
	}

	wfcFeatSuppressOrDeleteOptions_ptr options = wfcFeatSuppressOrDeleteOptions::create();
	if (clip) {
//		options->append(wfcFEAT_SUPP_OR_DEL_CLIP_ALL);
		options->append(wfcFEAT_SUPP_OR_DEL_CLIP);
		options->append(wfcFEAT_SUPP_OR_DEL_INDIV_GP_MEMBERS);
		options->append(wfcFEAT_SUPP_OR_DEL_CLIP_INDIV_GP_MEMBERS);
	}
	else {
		//options->append(wfcFEAT_SUPP_OR_DEL_CLIP);
		options->append(wfcFEAT_SUPP_OR_DEL_NO_OPTS);
	}

	wsolid->SuppressFeatures(feat_ids, options, 0);
}

void JCFeature::resume(JCSession* jcsess_ptr, xstring modelName, std::vector<int> featIds) {
	JCConnection connHandler;

	if (featureDebug) cout << "resume: " << jcsess_ptr << endl;

	xstring cid = jcsess_ptr->getConnectionId();
	pfcSession_ptr sess = connHandler.getSession(cid);
	if (sess == nullptr) {
		throw std::exception("Could not get Creo Session in native library!");
	}

	pfcModel_ptr model = nullptr;
	if (modelName.IsNull()) {
		model = sess->GetCurrentModel();
	}
	else {
		model = sess->GetModelFromFileName(modelName);
	}

	if (model == nullptr) {
		throw std::exception("Model is null or not found!");
	}
	if (!pfcSolid::isObjKindOf(model)) {
		throw std::exception("Model is not a solid!");
	}
	if (featureDebug) cout << "Model: " << model->GetDescr()->GetFileName() << "\n";

	pfcSolid_ptr solid = pfcSolid::cast(model);
	//pfcFeature_ptr feature = solid->GetFeatureById(featureId);
	wfcWSolid_ptr wsolid = wfcWSolid::cast(solid);

	xintsequence_ptr feat_ids = xintsequence::create();
	size_t len = featIds.size();
	for (int i = 0; i < len; i++) {
		feat_ids->append(featIds[i]);
	}

	wfcFeatResumeOptions_ptr options = wfcFeatResumeOptions::create();
	options->append(wfcFEAT_RESUME_INCLUDE_PARENTS);

	wsolid->ResumeFeatures(feat_ids, options, 0);
}

void JCFeature::deleteFeature(JCSession* jcsess_ptr, xstring modelName, std::vector<int> featIds, bool clip) {
	JCConnection connHandler;

	if (featureDebug) cout << "delete: " << jcsess_ptr << endl;

	xstring cid = jcsess_ptr->getConnectionId();
	pfcSession_ptr sess = connHandler.getSession(cid);
	if (sess == nullptr) {
		throw std::exception("Could not get Creo Session in native library!");
	}

	pfcModel_ptr model = nullptr;
	if (modelName.IsNull()) {
		model = sess->GetCurrentModel();
	}
	else {
		model = sess->GetModelFromFileName(modelName);
	}

	if (model == nullptr) {
		throw std::exception("Model is null or not found!");
	}
	if (!pfcSolid::isObjKindOf(model)) {
		throw std::exception("Model is not a solid!");
	}
	if (featureDebug) cout << "Model: " << model->GetDescr()->GetFileName() << "\n";

	pfcSolid_ptr solid = pfcSolid::cast(model);
	//pfcFeature_ptr feature = solid->GetFeatureById(featureId);
	wfcWSolid_ptr wsolid = wfcWSolid::cast(solid);

	xintsequence_ptr feat_ids = xintsequence::create();
	size_t len = featIds.size();
	for (int i = 0; i < len; i++) {
		feat_ids->append(featIds[i]);
	}

	wfcFeatSuppressOrDeleteOptions_ptr options = wfcFeatSuppressOrDeleteOptions::create();
	if (clip) {
		//options->append(wfcFEAT_SUPP_OR_DEL_CLIP_ALL);
		options->append(wfcFEAT_SUPP_OR_DEL_CLIP);
	}
	else {
		//options->append(wfcFEAT_SUPP_OR_DEL_CLIP);
		options->append(wfcFEAT_SUPP_OR_DEL_NO_OPTS);
	}

	wsolid->DeleteFeatures(feat_ids, options, 0);
}

