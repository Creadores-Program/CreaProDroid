package org.CreadoresProgram.CreaProDroid.okhttp;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.ConnectionSpec;

import java.util.Arrays;
public class OkClients{
    private OkHttpClient clientHt = new OkHttpClient.Builder()
        .connectionSpecs(Arrays.asList(
            new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
                .build(),
            ConnectionSpec.COMPATIBLE_TLS
        ))
        .build();
    private OkHttpClient clientHtIa = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(360, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .connectionSpecs(Arrays.asList(
            new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
                .build(),
            ConnectionSpec.COMPATIBLE_TLS
        ))
        .build();
    private static OkClients instance;
    public static OkClients getInstance(){
      if(instance == null){
        instance = new OkClients();
      }
      return instance;
    }
    private OkClients(){}
    public OkHttpClient getClient(){
      return this.clientHt;
    }
    public OkHttpClient getClientIA(){
      return this.clientHtIa;
    }
}
