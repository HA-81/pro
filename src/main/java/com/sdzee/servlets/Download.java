package com.sdzee.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet( urlPatterns = "/fichiers/*", initParams = @WebInitParam( name = "chemin", value = "/Users/Ali/fichiers/" ) )
public class Download extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final int   TAILLE_TAMPON    = 10240; // 10ko

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /*
         * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( "chemin" );

        /*
         * r�cup�ration du chemin au sein de l'URL de la requ�te
         */
        String fichierRequis = request.getPathInfo();

        /* V�rifie q'un fichier a bien �t� fourni */
        if ( fichierRequis == null ) {
            /*
             * Si non, alors on envoie une erreur 404, qui signifie que la
             * ressource demand�e n'existe pas
             */
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        /*
         * D�code le nom de fichier r�cup�r�, susceptible de contenir des
         * espaces et autres caract�res sp�ciaux, et pr�pare l'objet File
         */
        fichierRequis = URLDecoder.decode( fichierRequis, "UTF-8" );
        File fichier = new File( chemin, fichierRequis );

        /* V�rifie que le fichier existe bien */
        if ( !fichier.exists() ) {

            /*
             * Si non, alors on envoie une erreur 404, qui signifie que la
             * ressource demand�e n'existe pas
             */
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        /* R�cup�re le type du fichier */
        String type = getServletContext().getMimeType( fichier.getName() );

        /*
         * Si le type de fichier est inconnu, alors on initialise un type par
         * d�faut
         */
        if ( type == null ) {
            type = "application/octet-stream";
        }

        /* Initialise la r�ponse HTTP */
        response.reset();
        response.setBufferSize( TAILLE_TAMPON );
        response.setContentType( type );
        response.setHeader( "Content-Length", String.valueOf( fichier.length() ) );
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"" );

        /* Pr�pare les flux */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* ouvre les flux */
            entree = new BufferedInputStream( new FileInputStream( fichier ), TAILLE_TAMPON );
            sortie = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );

            /* Lit le fichier et �crit son contenu dans la r�ponse HTTP */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } finally {
            sortie.close();
            entree.close();
        }
    }
}