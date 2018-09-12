# ![](https://github.com/DobyLov/ob_IHM/blob/master/src/assets/Mini_4Git_OBLogoNoBkg.png) opusbeaute_middleware
Est l'API, le Middlware du logiciel OpusBeaute.

## Objectif du logiciel OpusBeauté
La mission du logiciel est d'augmenter la qualité de service des Rendez-Vous. Le vecteur d'amélioration est situé après la plannification d'un rendez-vous et intervient un jour avant le Rdv plannifié.
Le système envoi la veille, un mail et / ou un SMS de rappel de Rendez-vous avec les informations liées à la prestation, le lieu de réalisation et le praticien(ne) 
 
### Fonctionnalités du middleware
Le Middleware opusbeauté est l' API assurant la persistance des données dans une BDD et la mise à disposition de WebService REST avec une gestion des règles métier.  
 
La liste des services REST son consultables via le WADL (Web Application Document Language): [WADL](https://github.com/DobyLov/opusbeaute_middleware/blob/master/src/main/doc/application.wadl) 


### Prérequis
Le MiddleWare s'appuie sur une base de donnée SQL(PostGres) pour la persistance des données.

Projet en Java (MiddleWare): [OpusBeaute_MiddleWare](https://github.com/DobyLov/opusbeaute_middleware)

### Version des outils de développement
![](https://img.shields.io/badge/Java%20:-V%208.0-orange.svg) ![](https://img.shields.io/badge/Maven%20:-V%202.4-blue.svg) ![](https://img.shields.io/badge/PostgresSQL%20:-V%209.6-orange.svg) ![](https://img.shields.io/badge/Wildfly%20CLI%20:-V%2013-orange.svg) 

### Documentation Application
JavaDoc : [Documentation](https://github.com/DobyLov/opusbeaute_middleware/blob/master/doc/index.html)


### compilation du Projet

Aller dans le repertoire du projet:

	mvn clean install

