package com.sdzee.servlets;

import java.io.IOException;

import com.sdzee.beans.Fichier;
import com.sdzee.forms.UploadForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet( urlPatterns = "/upload", initParams = @WebInitParam( name = "chemin", value = "/Users/Ali/fichiers/" ) )
@MultipartConfig( location = "C:/Users/Ali/fichiers", maxFileSize = 10 * 1024 * 1024, maxRequestSize = 5 * 10 * 1024
        * 1024, fileSizeThreshold = 1024 * 1024 )
public class Upload extends HttpServlet {
    private static final long  serialVersionUID = 1L;
    public static final String CHEMIN           = "chemin";

    public static final String ATT_FICHIER      = "fichier";
    public static final String ATT_FORM         = "form";

    public static final String VUE              = "/WEB-INF/upload.jsp";

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Affichage de la page d'upload */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /*
         * Lecture du paramètre 'chemin' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( CHEMIN );

        /* Préparation de l'objet formulaire */
        UploadForm form = new UploadForm();

        /* Traitement de la requête et récupération du bean en résultant */
        Fichier fichier = form.enregistrerFichier( request, chemin );

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_FICHIER, fichier );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}
