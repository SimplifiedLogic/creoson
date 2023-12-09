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
#include "JCFile.h"
#include "JCConnection.h"
#include "JCSession.h"
#include "jshell_utils.h"
#include "com_simplifiedlogic_nitro_jlink_cpp_JCFile.h"

bool fileDebug = false;

#ifdef __cplusplus
extern "C" {
#endif
	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFile_setDebug
	(JNIEnv*, jobject, jboolean debug)
	{
		fileDebug = (bool)debug;
	}

	JNIEXPORT void JNICALL Java_com_simplifiedlogic_nitro_jlink_cpp_JCFile_featureSuppress
	(JNIEnv* env, jobject, jstring sessionIdIn, jstring modelNameIn, jint featId)
	{
		if (fileDebug) puts("File Feature Suppress:");

		const char* sessionIdStr = (*env).GetStringUTFChars(sessionIdIn, 0);
		xstring sessionId = sessionIdStr;
		env->ReleaseStringUTFChars(sessionIdIn, sessionIdStr);
		if (fileDebug) cout << "sessionId: " << sessionId << endl;

		xstring modelName = xstringnil;
		if (modelNameIn != nullptr) {
			const char* modelNameStr = (*env).GetStringUTFChars(modelNameIn, 0);
			modelName = modelNameStr;
			env->ReleaseStringUTFChars(modelNameIn, modelNameStr);
		}
		if (fileDebug) cout << "modelName: " << modelName << endl;

		JCFile fileHandler;

		try {
			JCSession sessHandler;
			JCSession* jcsess_ptr = sessHandler.getSession(sessionId);
			if (jcsess_ptr == nullptr) {
				jcsess_ptr = sessHandler.createSession(sessionId);
			}
			if (fileDebug) cout << "session ptr: " << jcsess_ptr << endl;

			fileHandler.suppressFeature(jcsess_ptr, modelName, featId);
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

	}

#ifdef __cplusplus
}
#endif

void JCFile::suppressFeature(JCSession* jcsess_ptr, xstring modelName, int featId)
{
	JCConnection connHandler;

	if (fileDebug) cout << "suppress: " << jcsess_ptr << endl;

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
	if (fileDebug) cout << "Model: " << model->GetDescr()->GetFileName() << "\n";

	pfcSolid_ptr solid = pfcSolid::cast(model);
	//pfcFeature_ptr feature = solid->GetFeatureById(featureId);
	wfcWSolid_ptr wsolid = wfcWSolid::cast(solid);

	xintsequence_ptr feat_ids = xintsequence::create();
	feat_ids->append(featId);

	wfcFeatSuppressOrDeleteOptions_ptr options = wfcFeatSuppressOrDeleteOptions::create();
	// assume always clipping
	options->append(wfcFEAT_SUPP_OR_DEL_CLIP);

	wsolid->SuppressFeatures(feat_ids, options, 0);
}
