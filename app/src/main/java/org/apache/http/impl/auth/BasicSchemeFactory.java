package org.apache.http.impl.auth;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

@Deprecated
public class BasicSchemeFactory implements AuthSchemeFactory {
    public AuthScheme newInstance(HttpParams params) {
        return new BasicScheme();
    }
}
