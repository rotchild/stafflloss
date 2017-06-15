package com.cx.utils;

import android.text.TextUtils;

public class MRegex {
public static boolean isRightCaseNo(String caseNoStr){
String caseNoRegex="[A-Z_0-9]{22}";
if (TextUtils.isEmpty(caseNoStr)) return false;
else return caseNoStr.matches(caseNoRegex);	
}

public static boolean isIPV4(String ipStr){
String isIPRegex="^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
if (TextUtils.isEmpty(ipStr)) return false;
else return ipStr.matches(isIPRegex);	
}

public static boolean isPort(String portStr){
String portRegex="^[0-9]{1,5}$";
if (TextUtils.isEmpty(portStr)) return false;
else return portStr.matches(portRegex);	
}

}
