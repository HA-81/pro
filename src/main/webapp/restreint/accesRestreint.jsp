<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Accès restreint</title>
</head>
<body>
	<p>Vous êtes connecté(e) avec l'adresse
		${sessionScope.sessionUtilisateur.email}, vous avez bien accès à
		l'espace restreint.</p>
</body>
</html>