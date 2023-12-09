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

#include "jshell_utils.h"

jint handleCipXException(JNIEnv* env, cipXException* ciperr) {
	cout << "cip exception caught: " << ciperr << endl;
	jclass exClass = env->FindClass("com/simplifiedlogic/nitro/rpc/JCException");
	return env->ThrowNew(exClass, ciperr->getDescription());
}

jint handlePfcXToolkitError(JNIEnv* env, pfcXToolkitError* e) {
	cout << "exception caught: " << e << endl;
	cout << "func: " << e->GetToolkitFunctionName() << " meth: " << e->GetMethodName() << endl;
	jstring msgstr = env->NewStringUTF(e->GetMessage());


	jclass exClass = env->FindClass("com/simplifiedlogic/nitro/rpc/JCToolkitException");
	// IMPORTANT: use javap -s com.simplifiedlogic.nitro.rpc.JCToolkitException (example) to find signatures for below
	jmethodID ctor = env->GetMethodID(exClass, "<init>", "(Ljava/lang/String;I)V");

	jobject obj = env->NewObject(exClass, ctor, msgstr, e->GetErrorCode());

//		env->ThrowNew(exClass, "Toolkit Error: "+e->GetMessage());
	return env->Throw((jthrowable)obj);
}

jint handlePfcXPFC(JNIEnv* env, pfcXPFC* xpfcerr) {
	cout << "pfcXPFC exception caught: " << xpfcerr << endl;
	jclass exClass = env->FindClass("com/simplifiedlogic/nitro/rpc/JCException");
	return env->ThrowNew(exClass, xpfcerr->GetMessage());
}

jint handleException(JNIEnv* env, const std::exception& ex) {
	cout << "standard exception caught: " << ex.what() << endl;
	jclass exClass = env->FindClass("com/simplifiedlogic/nitro/rpc/JCException");
	return env->ThrowNew(exClass, ex.what());
}
