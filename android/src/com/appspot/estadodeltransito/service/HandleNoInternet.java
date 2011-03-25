package com.appspot.estadodeltransito.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.Context;

import com.appspot.estadodeltransito.service.exception.SteamServiceException;
import com.appspot.estadodeltransito.util.NoInternetToast;

public class HandleNoInternet implements InvocationHandler {
	private final Object delegate; // set fields from constructor args
	private final Context ctx;

	public HandleNoInternet(Context ctx, Object delegate) {
		this.ctx = ctx;
		this.delegate = delegate;
	}

	public Object invoke(Object proxy, Method method, Object[] args) {

		try {
			// invoke the method on the delegate and handle the exception
			return method.invoke(delegate, args);
		} catch (Exception ex) {
			if (ex.getCause() instanceof SteamServiceException) {
				NoInternetToast.show(ctx);
			} else {
				throw new RuntimeException(ex);
			}
		}

		return null;
	}
}
