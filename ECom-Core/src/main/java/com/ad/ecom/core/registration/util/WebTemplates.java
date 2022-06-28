package com.ad.ecom.core.registration.util;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;

public final class WebTemplates {

    public static String RegistrationConfirmationTemplate(EcomUser user, String URL) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tcenter {\n" +
                "\t\t\t\tmargin: 0 auto !important;\n" +
                "\t\t\t}" +
                "\t\t\tp {\n" +
                "\t\t\t\tbackground-color: lightsteelblue;\n" +
                "\t\t\t\tcolor: white;" +
                "\t\t\t\tpadding-top: 20px;\n" +
                "\t\t\t\tpadding-bottom: 20px;\n" +
                "\t\t\t\tpadding-left: 0;\n" +
                "\t\t\t\tpadding-right: 0;\n" +
                "\t\t\t\tfont-family: \"Lucida Console\", \"Courier New\", monospace;\n" +
                "\t\t\t}\n" +
                "\t\t\th3 {\n" +
                "\t\t\t\tbackground-color: white;\n" +
                "\t\t\t}\n" +
                "\t\t\t.verify {\n" +
                "\t\t\t\tbackground-color: green;\n" +
                "\t\t\t\tpadding: 14px 25px;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t\tcolor: white !important;\n" +
                "\t\t\t\ttext-decoration: unset;\n" +
                "\t\t\t\tdisplay: inline-block;\n" +
                "\t\t\t}\n" +
                "\t\t\t.verify:hover {\n" +
                "\t\t\t\tcolor:white;" +
                "\t\t\t\tbackground-color: forestgreen;\n" +
                "\t\t\t}\n" +
                "\t\t\t@media only screen and (min-width: 930px) {\n" +
                "\t\t\t\tcenter {\n" +
                "\t\t\t\twidth: 50%;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<center>\n" +
                "\t\t<p><h3>\n" +
                "\t\t\tHi " + user.getFirstName() + " " + user.getLastName() + "!<br/>\n" +
                "\t\t\tWelcome to ECOM<br/><br/>\n" +
                "\t\t\t<span style=\"cursor:pointer\"><a class=\"verify\" href=\""+ URL +"\" target=\"_blank\">Activate your ECOM account</a></span>\n" +
                "\t\t</h3></p>\n" +
                "\t\t</center>\n" +
                "\t</body>" +
                "</html>";
    }

    public static String UserWebTemplate(String serverUrl, String contextPath) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tcenter {\t\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t\t.logout {\n" +
                "\t\t\t\tfloat: right;\n" +
                "\t\t\t\tpadding-right: 2%;\n" +
                "\t\t\t}\n" +
                "\t\t\t@media only screen and (min-width: 768px) {\n" +
                "  \t\t\t\t/* For mobile phones: */\n" +
                "  \t\t\t\tcenter {\n" +
                "    \t\t\t\twidth: 50%;\n" +
                "  \t\t\t\t}\n" +
                "  \t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"logout\"><h2><a href=\"" + serverUrl + contextPath + "/logout\" target = \"_self\">Logout</a></h2></div><br>\n" +
                "\t\t<center>\n" +
                "\t\t<p>\n" +
                "\t\t\t<h2>WELCOME USER</h2>\n" +
                "\t\t</p>\n" +
                "\t\t</center>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public static String AdminWebTemplate(String serverUrl, String contextPath) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tcenter {\t\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t\t.logout {\n" +
                "\t\t\t\tfloat: right;\n" +
                "\t\t\t\tpadding-right: 2%;\n" +
                "\t\t\t}\n" +
                "\t\t\t@media only screen and (min-width: 768px) {\n" +
                "  \t\t\t\t/* For mobile phones: */\n" +
                "  \t\t\t\tcenter {\n" +
                "    \t\t\t\twidth: 50%;\n" +
                "  \t\t\t\t}\n" +
                "  \t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"logout\"><h2><a href=\"" + serverUrl + contextPath + "/logout\" target = \"_self\">Logout</a></h2></div><br>\n" +
                "\t\t<center>\n" +
                "\t\t<p>\n" +
                "\t\t\t<h2>WELCOME ADMIN</h2>\n" +
                "\t\t</p>\n" +
                "\t\t</center>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public static String SellerWebTemplate(String serverUrl, String contextPath) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tcenter {\t\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t\t.logout {\n" +
                "\t\t\t\tfloat: right;\n" +
                "\t\t\t\tpadding-right: 2%;\n" +
                "\t\t\t}\n" +
                "\t\t\t@media only screen and (min-width: 768px) {\n" +
                "  \t\t\t\t/* For mobile phones: */\n" +
                "  \t\t\t\tcenter {\n" +
                "    \t\t\t\twidth: 50%;\n" +
                "  \t\t\t\t}\n" +
                "  \t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"logout\"><h2><a href=\"" + serverUrl + contextPath + "/logout\" target = \"_self\">Logout</a></h2></div><br>\n" +
                "\t\t<center>\n" +
                "\t\t<p>\n" +
                "\t\t\t<h2>WELCOME SELLER</h2>\n" +
                "\t\t</p>\n" +
                "\t\t</center>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public static String getAccountVerificationSuccessTemplate(String serverUrl, String contextPath) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tcenter {\t\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t\t@media only screen and (min-width: 768px) {\n" +
                "  \t\t\t\t/* For mobile phones: */\n" +
                "  \t\t\t\tcenter {\n" +
                "    \t\t\t\twidth: 50%;\n" +
                "  \t\t\t\t}\n" +
                "  \t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<center>\n" +
                "\t\t<p>\n" +
                "\t\t\t<h2 style=\"color:green\">Account Activation Successful!<br>\n" +
                "\t\t\t<a href=\"" + serverUrl + contextPath + "/login\" target = \"_blank\">Login</a></h2>\n" +
                "\t\t</p>\n" +
                "\t\t</center>\n" +
                "\t</body>\n" +
                "</html>";
    }
}
