package com.sdzee.servlets;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.sdzee.beans.Utilisateur;
import com.sdzee.forms.ConnexionForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet( "/connexion" )
public class Connexion extends HttpServlet {
    private static final long  serialVersionUID          = 1L;

    public static final String ATT_USER                  = "utilisateur";
    public static final String ATT_FORM                  = "form";
    public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
    public static final String ATT_SESSION_USER          = "sessionUtilisateur";
    public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
    public static final String FORMAT_DATE               = "dd/MM/yyyy HH:mm:ss";
    public static final String CHAMP_MEMOIRE             = "memoire";
    public static final int    COOKIE_MAX_AGE            = 60 * 60 * 24 * 365;      // 1an
    public static final String VUE                       = "/WEB-INF/connexion.jsp";

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Tentative de r�cup�ration du cookie depuis la requ�te */
        String derniereConnexion = getCookieValue( request, COOKIE_DERNIERE_CONNEXION );
        /* Si le cookie existe, alors calcul de la dur�e */
        if ( derniereConnexion != null ) {
            /* R�cup�ration de la date courante */
            DateTime dtCourante = new DateTime();
            /* R�cup�ration de la date pr�sente dans le cookie */
            DateTimeFormatter formatter = DateTimeFormat.forPattern( FORMAT_DATE );
            DateTime dtDerniereConnexion = formatter.parseDateTime( derniereConnexion );
            /* Calcul de la dur�e de l'intervalle */
            Period periode = new Period( dtDerniereConnexion, dtCourante );
            /* Formatage de la dur�e de l'intervalle */
            PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                    .appendYears().appendSuffix( "an", "ans" )
                    .appendMonths().appendSuffix( "mois" )
                    .appendDays().appendSuffix( "jour", "jours" )
                    .appendHours().appendSuffix( "heure", "heures" )
                    .appendMinutes().appendSuffix( "minute", "minutes" )
                    .appendSeparator( "et" )
                    .appendSeconds().appendSuffix( "seconde", "secondes" )
                    .toFormatter();
            String intervalleConnexions = periodFormatter.print( periode );
            /* Ajout de l'intervalle en tant qu'attribut de la requ�te */
            request.setAttribute( ATT_INTERVALLE_CONNEXIONS, intervalleConnexions );
        }
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Pr�paration de l'objet formulaire */
        ConnexionForm form = new ConnexionForm();

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Utilisateur utilisateur = form.connecterUtilisateur( request );

        /* R�cup�ration de la session depuis la requ�te */
        HttpSession session = request.getSession();

        /*
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur � la session, sinon suppression du bean de la session.
         */
        if ( form.getErreurs().isEmpty() ) {
            session.setAttribute( ATT_SESSION_USER, utilisateur );
        } else {
            session.setAttribute( ATT_SESSION_USER, null );
        }

        /* Si et seulement si la case du formulaire est coch�e */
        if ( request.getParameter( CHAMP_MEMOIRE ) != null ) {
            /* R�cup�ration de la date courante */
            DateTime dt = new DateTime();
            /* Formatage de la date et conversion en texte */
            DateTimeFormatter formatter = DateTimeFormat.forPattern( FORMAT_DATE );
            String dateDerniereConnexion = dt.toString( formatter );
            /* Cr�ation du cookie, et ajout � la r�ponse HTTP */
            setCookie( response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE );
        } else {
            /* Demande de suppression du cookie du navigateur */
            setCookie( response, COOKIE_DERNIERE_CONNEXION, "", 0 );
        }
        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    /*
     * M�thode utilitaire g�rant la cr�ation d'un cookie et son ajout � la
     * r�ponse HTTP.
     */
    private static void setCookie( HttpServletResponse response, String nom, String valeur, int maxAge ) {
        Cookie cookie = new Cookie( nom, valeur );
        cookie.setMaxAge( maxAge );
        response.addCookie( cookie );
    }

    /*
     * M�thode utilitaire g�rant la r�cup�ration de la valeur d'un cookie donn�
     * depuis la requ�te HTTP.
     */
    private static String getCookieValue( HttpServletRequest request, String nom ) {
        Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {
            for ( Cookie cookie : cookies ) {
                if ( cookie != null && nom.equals( cookie.getName() ) ) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
