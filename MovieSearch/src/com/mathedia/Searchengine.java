package com.mathedia;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Searchengine {
	
    private String name1;
    private String name2;
    
    public Searchengine() {}
    
    public Searchengine(String name1, String name2) {
    	this.name1 = name1;
    	this.name2 = name2;
    }
    private List<String> match = new ArrayList();

    public List<String> getMatch() {
        return match;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    //Besorgt alle Links, die von einer WikiSeite rausgehen
    public static List<String> getLinks(String s) throws Exception {
        //Rufe die XML von der Mediawiki APi ab, und speichere sie im Sting r
        s = s.replace(' ', '_');
        //Prüfe, ob es zu dem Schauspieler eine eigene Filmography Seite gibt
        String url = "http://en.wikipedia.org/w/api.php?format=xml&action=opensearch&limit=5&search=" + s;
        URL u = new URL(url);
        String r = new Scanner(u.openStream()).useDelimiter("\\Z").next();

        if (r.contains(s + "_filmography")) {
            s = s + "_filmography";
        }

        //Holt die XML des Schauspielers oder seiner Filmography Seite
        url = "http://en.wikipedia.org/w/api.php?format=xml&action=query&prop=links&pllimit=500&titles=" + s;
        u = new URL(url);
        r = new Scanner(u.openStream()).useDelimiter("\\Z").next();

        //SAXBuilder konvertiert die XML für die Java-Verarbeitung
        SAXBuilder saxBuilder = new SAXBuilder();
        List<String> list = new ArrayList<String>();
        try {
            Document doc = saxBuilder.build(new StringReader(r));

            //Durchforstet die XML vom Wurzelelement bis zu den gewünschten Elemente, wo die Links stehen
            Element rootelement = doc.getRootElement();
            Element links = rootelement.getChild("query").getChild("pages").getChild("page").getChild("links");

            List<Element> linklist = links.getChildren("pl");

            //Schreibt alle Links als Strings in eine Liste
            for (int i = 0; i <= linklist.size() - 1; i++) {
                Element e = linklist.get(i);
                String t = e.getAttribute("title").getValue();
                list.add(t);
            }

        } catch (JDOMException e) {
            // handle JDOMException
            e.printStackTrace();
        } catch (IOException e) {
            // handle IOException
            e.printStackTrace();
        }

        return list;
    }

    //Erstellt eine neue List3, in der nur die Links stehen, die auf beiden Schauspielerseiten auftauchen
    public static List<String> getMatches(List<String> m, List<String> n) throws Exception {
        List<String> r = new ArrayList<String>();
        for (int i = 0; i <= m.size() - 1; i++) {
            if (n.contains(m.get(i))) {
                r.add(m.get(i));
            }
        }
        return r;
    }

    //Prüfe, ob die Links zu Filmseiten gehen
    public static List<String> getFilms(List<String> l) throws Exception {
        List<String> filmlist = new ArrayList<String>();

        //Durchläuft alle Links
        for (int i = 0; i <= l.size() - 1; i++) {
            String linktitel = l.get(i);
            linktitel = linktitel.replace(' ', '_');   //Die URL muss codiert werden, Leerzeichen werden durch Unterstrich erstetzt, &-Zeichen durch %26
            linktitel = linktitel.replaceAll("&", "%26"); //Für umfangreichere Projekte eventuell die Klassen java.net.URLEncoder, java.net.URLDecoder verwenden
            String url = "http://en.wikipedia.org/w/api.php?format=xml&action=query&prop=categories&titles=" + linktitel;
            URL u = new URL(url);
            String r = new Scanner(u.openStream()).useDelimiter("\\Z").next();

            SAXBuilder saxBuilder = new SAXBuilder();
            List<String> list = new ArrayList<String>();
            try {
                Document doc = saxBuilder.build(new StringReader(r));

                Element rootelement = doc.getRootElement();
                Element categories = rootelement.getChild("query").getChild("pages").getChild("page").getChild("categories");

                if (categories != null) {
                    List<Element> categorielist = categories.getChildren("cl");

                    //Wenn die Wiki-Seite zu einer Categorie gehört, die auf Film hindeutet, wird die Seite zur Filmliste hinzugefügt
                    for (int j = 0; j <= categorielist.size() - 1; j++) {
                        Element e = categorielist.get(j);
                        String categorietitel = e.getAttribute("title").getValue();
                        if (categorietitel.contains("films")) {
                            filmlist.add(linktitel);
                            break;
                        }
                    }
                }

            } catch (JDOMException e) {
                // handle JDOMException
                e.printStackTrace();
            } catch (IOException e) {
                // handle IOException
                e.printStackTrace();
            }
        }
        return filmlist;
    }

    public void eval() {
        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();

        //getLinks() wird mit den eingegebenen Namen aufgerufen
        try {
            list1 = getLinks(name1);
        } catch (Exception ex) {
            Logger.getLogger(Searchengine.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            list2 = getLinks(name2);
        } catch (Exception ex) {
            Logger.getLogger(Searchengine.class.getName()).log(Level.SEVERE, null, ex);
        }

        //getMatches() wird mit den gerade erstellten Listen aufgerufen
        try {
            match = getMatches(list1, list2);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Sortiere alle Links aus, die keine Filme sind
        try {
            match = getFilms(match);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        if (match.size() > 0) {
                for (int i = 0; i <= match.size() - 1; i++) {
                String temp = match.get(i);
                temp = temp.replace('_', ' ');
                temp = temp.replaceAll("%26", "&");
                match.set(i, temp);
            }
        }
        if (match.size() == 0) {
            match.add("No movie found.");
        }

        return;
    }
}
