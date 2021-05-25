package view.tools;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * Classe avec plein de méthodes utiles pour des conversion selon les différents types de dates
 */
public class OutilsDate {

    /**
     * Permet de convertir un objet LocalDate à un objet Date
     * @param pfDate LocalDate à convertir
     * @return la Date converti
     */
    public static Date localDateToDate(LocalDate pfDate){
        Date SQLdate = Date.valueOf(pfDate) ;
        return SQLdate ;
    }

    /**
     * Permet de convertir un objet Date en String
     * @param pfDate La date a convertir
     * @return Un string
     */
    public static String dateToString(Date pfDate){
        Calendar calDate = dateToCalendar(pfDate) ;
        String strDate = calDate.get(Calendar.DAY_OF_MONTH) + "-" + calDate.get(Calendar.MONTH) +"-" + calDate.get(Calendar.YEAR) ;
        return strDate ;
    }

    /**
     * Permet de convertir un String a un objet Date
     * @param pfString Le String a convertir
     * @return la Date converti
     */
    public static Date stringToDate(String pfString){
        Date SQLdate = Date.valueOf(pfString) ;
        return SQLdate ;
    }

    /**
     * Permet de convertir un Date en un objet LocalDate
     * @param pfDate Le Date a convertir
     * @return la LocalDate converti
     */
    public static LocalDate dateToLocalDate(Date pfDate){
        Calendar calDate = dateToCalendar(pfDate) ;
        System.out.println(calDate.get(Calendar.YEAR));
        System.out.println(calDate.get(Calendar.MONTH));
        System.out.println(calDate.get(Calendar.DAY_OF_MONTH));

        LocalDate date = LocalDate.of(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH)+1, calDate.get(Calendar.DAY_OF_MONTH)) ;
        return date ;
    }

    /**
     * Retourner un calendar a partir d'une SQL date
     * Utilisation :
     *         int month = cal.get(Calendar.MONTH);
     *         int day = cal.get(Calendar.DAY_OF_MONTH);
     *         int year = cal.get(Calendar.YEAR);     *
     *
     * @param pfDate : SQL Date
     * @return Calendar value
     */
    public static Calendar dateToCalendar(Date pfDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pfDate);
        return cal ;
    }
}
