<%-- 
    Document   : login
    Created on : 14 nov. 2015, 15:50:12
    Author     : Yunho
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Authentification</h1>
        <form method="post" action="${pageContext.request.contextPath}/authentification">
            <table>
                <tr>
                    <td>Utilisateur : </td>
                    <td><input type="text" name="user" /></td>
                </tr>
                <tr>
                    <td>Mot de passe : </td>
                    <td><input type="password" name="password" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" vlaue="Valider"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
