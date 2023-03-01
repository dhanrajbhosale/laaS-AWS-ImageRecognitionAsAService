package org.cse546.config;

import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.context.annotation.Configuration;

public class AWSConfiguration {


    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    public static BasicAWSCredentials getAWSCREDENTIALS() {
        BasicAWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(
                ACCESS_KEY,
                SECRET_KEY
        );
        return AWS_CREDENTIALS;

    }
}
