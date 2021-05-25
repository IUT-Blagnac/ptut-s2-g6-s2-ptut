------------------------------------------------------------------------
---------------------------- Version 0 ---------------------------------
------------------------------------------------------------------------

-------------------------------
------SUPPRESSION DES TABLES --
-------------------------------

DROP TABLE Employe;
DROP TABLE Role;
DROP TABLE NiveauCompetence;
DROP TABLE Niveau;
DROP TABLE Competence;
DROP TABLE Parametre;



-------------------------------
------ CREATION DES TABLES ----
-------------------------------
-- Ex : Marge de 5%, marge de 10%...
CREATE TABLE Parametre(
	margeBeneficiaire NUMBER(5,2)
);

-- Ex : Développeur Java / Développeur Web / Administrateur Système...
CREATE TABLE Competence(
	idCompetence NUMBER(3),
	nom VARCHAR(50),
	CONSTRAINT PK_Competence PRIMARY KEY (idCompetence)	
);

-- Ex : Débutant / Confirmé / Expert
CREATE TABLE Niveau(
	idNiveau NUMBER(1),
	intitule VARCHAR(30),
	CONSTRAINT PK_Niveau PRIMARY KEY (idNiveau)	
);

-- Ex : Développeur Java Confirmé / Développeur Web Expert / Administrateur Système Débutant...
CREATE TABLE NiveauCompetence(
	idCompetence NUMBER(3),
	idNiveau NUMBER(1),	
	tauxHoraire NUMBER(4,2),	-- ex : 15€/h pour Développeur Java Débutant
	CONSTRAINT PK_NiveauCompetence PRIMARY KEY (idCompetence, idNiveau),
	CONSTRAINT CK_NiveauCompetence_TauxHor CHECK (tauxHoraire > 0 ),
	CONSTRAINT FK_NiveauCompetence_Compet FOREIGN KEY (idCompetence) REFERENCES Competence(idCompetence),
	CONSTRAINT FK_NiveauCompetence_Niveau FOREIGN KEY (idNiveau) REFERENCES Niveau(idNiveau)
);

-- EX: Chef de projet / Employé
CREATE TABLE Role(
	idRole NUMBER(1),
	nom VARCHAR(30),
	CONSTRAINT PK_Role PRIMARY KEY (idRole)	
);

-- Ex : Eric Dupont, employé de notre ESN en tant que Développeur Web Expert
CREATE TABLE Employe(
	idEmploye NUMBER(3),
	nom VARCHAR(25),
	prenom VARCHAR(15),
	login VARCHAR(15),		
	motDePasse VARCHAR(70), 
	estActif NUMBER(1) DEFAULT 1, -- par défaut, un employé est actif
	idRole NUMBER(1),
	idCompetence NUMBER(3),
	idNiveau NUMBER(1),
	CONSTRAINT PK_Employe PRIMARY KEY (idEmploye),
	CONSTRAINT FK_Employe_Role FOREIGN KEY (idRole) REFERENCES Role(idRole),
	CONSTRAINT FK_Employe_NiveauCompetence FOREIGN KEY (idCompetence, idNiveau) REFERENCES NiveauCompetence(idCompetence,idNiveau),
	-- Un employé a forcement un rôle
	CONSTRAINT NN_Employe_Role CHECK (idRole IS NOT NULL),
	-- Un employé a forcement un niveau de compétence...
	CONSTRAINT NN_Employe_Competence CHECK (idCompetence IS NOT NULL),
	CONSTRAINT NN_Employe_Niveau CHECK (idNiveau IS NOT NULL),
	-- estActif ne peut prendre que 2 valeurs : 1 (actif) ou 0 (inactif)
	CONSTRAINT CK_Employe_EstActif CHECK (estActif IN (0,1))
);


-------------------------------
------------ JEU ESSAI---------
-------------------------------
INSERT INTO Parametre VALUES (0.05);
INSERT INTO Parametre VALUES (0.1);
INSERT INTO Parametre VALUES (0.15);
INSERT INTO Parametre VALUES (0.2);

-- Séquence pour générer automatiquement la clé primaire d'une nouvelle compétence insérée dans la table
DROP SEQUENCE seq_id_competence;
CREATE SEQUENCE seq_id_competence
  MINVALUE 1  MAXVALUE 999999999999  
  START WITH 1 INCREMENT BY 1;

-- Competence
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Développeur Java');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Développeur Web');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Administrateur de bases de données');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Administrateur système');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Développeur Python');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Gestionnaire de Projet');
INSERT INTO Competence VALUES(seq_id_competence.NEXTVAL, 'Analyste-Concepteur');

-- Niveau
INSERT INTO Niveau VALUES(1,'Débutant') ;
INSERT INTO Niveau VALUES(2,'Confirmé') ;
INSERT INTO Niveau VALUES(3,'Expert') ;

-- NiveauCompetence
INSERT INTO NiveauCompetence VALUES(1,1,15);
INSERT INTO NiveauCompetence VALUES(1,2,20);
INSERT INTO NiveauCompetence VALUES(1,3,30);
INSERT INTO NiveauCompetence VALUES(2,1,16);
INSERT INTO NiveauCompetence VALUES(2,2,22);
INSERT INTO NiveauCompetence VALUES(2,3,33);
INSERT INTO NiveauCompetence VALUES(3,1,18);
INSERT INTO NiveauCompetence VALUES(3,2,24);
INSERT INTO NiveauCompetence VALUES(3,3,35);
INSERT INTO NiveauCompetence VALUES(4,1,19);
INSERT INTO NiveauCompetence VALUES(4,2,25);
INSERT INTO NiveauCompetence VALUES(4,3,37);
INSERT INTO NiveauCompetence VALUES(5,1,20);
INSERT INTO NiveauCompetence VALUES(5,2,30);
INSERT INTO NiveauCompetence VALUES(5,3,40);
INSERT INTO NiveauCompetence VALUES(6,1,30);
INSERT INTO NiveauCompetence VALUES(6,2,37);
INSERT INTO NiveauCompetence VALUES(6,3,45);
INSERT INTO NiveauCompetence VALUES(7,1,22);
INSERT INTO NiveauCompetence VALUES(7,2,33);
INSERT INTO NiveauCompetence VALUES(7,3,44);

-- Séquence pour générer automatiquement la clé primaire d'un nouveau rôle inséré dans la table
DROP SEQUENCE seq_id_role;
CREATE SEQUENCE seq_id_role
  MINVALUE 1  MAXVALUE 999999999999  
  START WITH 1 INCREMENT BY 1;

-- Role (droit acces application)
INSERT INTO Role VALUES(seq_id_role.NEXTVAL, 'Chef de Projet') ;
INSERT INTO Role VALUES(seq_id_role.NEXTVAL, 'Employe') ;

-- Employés

-- Séquence pour générer automatiquement la clé primaire d'un nouvel employé inséré dans la table
DROP SEQUENCE seq_id_employe;
CREATE SEQUENCE seq_id_employe
  MINVALUE 1  MAXVALUE 999999999999  
  START WITH 1 INCREMENT BY 1;
  
---------- LISTE DES MOT PASSE CHIFFRES ------------------
-- LR : 06d92ee9c796a7c2419a6ff814b2c022f13c2eae28289402f99e433ec5641
-- MFC : a0260a7de6450bfc86d8cfa8234bdb914077789131319de944da7771847d72
-- EP : dcc9be2679d9e1a4b7f2b90f67ee5638a679da66f1b6a39666c4b0fbd95ce3
-- FS : f71f7b443cf23e606f946dc774dcaac9a51d3e10df3bbfa0106351c56e0f93f
-- AP : 84810fea2375dab1b1c8417b8eed248488f55b00f091f4d4ef37dffca3b30
-- FP : 848951c761098afa2cff03ea819c3a55232116c7608fecb3e048586c38355e
-- LD : ab31ea4ae505371d328d117172caf1fe173ac2275c01017343e8156a50679c
-- YF : 99d1871b6ee8c413f6a84083e1ac3862efb785b94bb10936bc864ea7d864ae5
-- LN : 69a844cab6d32d01e1aad4e3682a2ed5da89bd7ec38cc827bc38293bba4ce27
-- NH : 901a6dbb9f97c7400595dc1901290775e1529ae3f8d9a5640bbe91c4bf41c93
-- MZ : 9b8db510ef42b8ed54a3712636fda55a4f8cfcd5493e20b74ab00d4f3979f2d
-- MT : 409521f02c8b29bd4f76b832de53a56fb64ac9085f6fdce1545f53c643b2219
-- OT : c2d4fe38448be66bac6fa893253b1618eb223f5dc1bc10d6f6ff8d2fb686f00
-- CC : a56362a10816abf206d72cb914e2d5ca454eb9c7e744f88b1a1422c379e9942
-- DV : 3df6d4d865fadd63a788de2eec3d24ec56f4fae1a1751217c8b1d8faf9133635
-- PST : c5d0e8952e13fc8a6ff50ccb9f511ed158bc143ae0a2e65fc0240288673fb
-- PSO : 44f6c6147aee816a3027822366a21efdcca05a8914226332c373a7f3ea4dc018
-- IO : 1295b0b84cea614a9331304f55324ef706392a177dfdbe961280fc6148eb9d
-- RB : eac8a36c12c94b93e22928c1174372d15dca0a5724481788a7a0901e05338
-- PRG : 60f3f58dfa13f5a5bcb28b2333ef37b7bb2d8760906d4ac71871c91b1137d7fc
-- PSO : 44f6c6147aee816a3027822366a21efdcca05a8914226332c373a7f3ea4dc018
-- JMB : 46005bc13877c0220c39f4ff45f3eeb93908f4a18e7e78800d6300e93b
-- JPA : e85d998148c540dbea905395a139b4eed97573a268e71e3485931dc0a8e99f7d
-- JBC : 42a37048cd79bd753133333da5a4949d78c3ca9e3ef789913e51f8af58474c8c
----------------------------------------------------------------------------------
  
    
-- pour simplifier, le login est égal au prénom de l'employé et son mot de passe est égal à ses initiales "classiques" dans FlopEdt
-- on insère d'abord les chef(fe)s de projet (on considère qu'ils/elles sont des employé.e.s compétent.e.s en 'Gestion de Projet'

-- Normalement, il n'est pas nécessaire de rappeler les colonnes de la table Employe car
-- on insère dans toutes les colonnes de cette table. Cependant, ici, cela nous permet de mieux comprendre les valeurs insérées
INSERT INTO Employe (idEmploye, nom,     prenom,     login,      motDePasse,                                                      estActif,    idRole, idCompetence, idNiveau) 
VALUES(seq_id_employe.NEXTVAL,  'Redon', 'Laurence', 'Laurence', '06d92ee9c796a7c2419a6ff814b2c022f13c2eae28289402f99e433ec5641',       1,         1,      6,            3   );
-- Laurence/LR

INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Canut','Marie-Françoise','Marie-Françoise','a0260a7de6450bfc86d8cfa8234bdb914077789131319de944da7771847d72', 1, 1, 6, 2);
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Pendaries', 'Esther','Esther','dcc9be2679d9e1a4b7f2b90f67ee5638a679da66f1b6a39666c4b0fbd95ce3',1,1,6,1);
-- Ci-dessous une chef de projet 'Inactive'
INSERT INTO Employe (idEmploye, nom,     prenom,     login,      motDePasse, estActif,  idRole, idCompetence, idNiveau) 
VALUES(seq_id_employe.NEXTVAL,  'Sedes', 'Florence', 'Florence', 'f71f7b443cf23e606f946dc774dcaac9a51d3e10df3bbfa0106351c56e0f93f', 0, 1, 6, 2);

-- Les "compétents" Java
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Péninou','André','André','84810fea2375dab1b1c8417b8eed248488f55b00f091f4d4ef37dffca3b30', 1, 2, 1, 2); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Pelleau','Fabrice','Fabrice','848951c761098afa2cff03ea819c3a55232116c7608fecb3e048586c38355e', 1, 2, 1, 1);
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Demay','Laurent','Laurent','ab31ea4ae505371d328d117172caf1fe173ac2275c01017343e8156a50679c', 1, 2, 1, 3);
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Formanczak','Yahn','Yahn','99d1871b6ee8c413f6a84083e1ac3862efb785b94bb10936bc864ea7d864ae5', 1, 2, 1, 1);

-- Les "compétents" Web
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Nonne','Laurent','Laurent','69a844cab6d32d01e1aad4e3682a2ed5da89bd7ec38cc827bc38293bba4ce27', 1, 2, 2, 2); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Hanine','Nabil','Nabil','901a6dbb9f97c7400595dc1901290775e1529ae3f8d9a5640bbe91c4bf41c93', 1, 2, 2, 1); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Zuckerberg','Mark','Mark','9b8db510ef42b8ed54a3712636fda55a4f8cfcd5493e20b74ab00d4f3979f2d', 1, 2, 2, 3);

-- Les "compétents" BD
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Tuffery','Michel','Michel','409521f02c8b29bd4f76b832de53a56fb64ac9085f6fdce1545f53c643b2219', 1, 2, 3, 3); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Teste','Olivier','Olivier','c2d4fe38448be66bac6fa893253b1618eb223f5dc1bc10d6f6ff8d2fb686f00', 1, 2, 3, 1); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Chrisment','Claude','Claude','a56362a10816abf206d72cb914e2d5ca454eb9c7e744f88b1a1422c379e9942', 1, 2, 3, 2); 

-- Les "compétents" Système
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Vielle','Daniel','Daniel','3df6d4d865fadd63a788de2eec3d24ec56f4fae1a1751217c8b1d8faf9133635', 1, 2, 4, 3); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Stolf','Patricia','Patricia','c5d0e8952e13fc8a6ff50ccb9f511ed158bc143ae0a2e65fc0240288673fb', 1, 2, 4, 2); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Ober','Iulan','Iulan','1295b0b84cea614a9331304f55324ef706392a177dfdbe961280fc6148eb9d', 1, 2, 4, 1); 

-- Les "compétents" Python
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Boulle','Rémi','Rémi','eac8a36c12c94b93e22928c1174372d15dca0a5724481788a7a0901e05338', 1, 2, 5, 1); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Renaud-Goud','Paul','Paul','60f3f58dfa13f5a5bcb28b2333ef37b7bb2d8760906d4ac71871c91b1137d7fc', 1, 2, 5, 3); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Sotin','Pascal','Pascal','44f6c6147aee816a3027822366a21efdcca05a8914226332c373a7f3ea4dc018', 1, 2, 5, 1); 

-- Les "compétents" Analyse-Conception
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Bruel','Jean-Michel','Jean-Michel','46005bc13877c0220c39f4ff45f3eeb93908f4a18e7e78800d6300e93b', 1, 2, 7, 2); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Anton','Jean-Paul','Jean-Paul','e85d998148c540dbea905395a139b4eed97573a268e71e3485931dc0a8e99f7d', 1, 2, 7, 1); 
INSERT INTO Employe VALUES (seq_id_employe.NEXTVAL,'Crampes','Jean-Bernard','Jean-Bernard','42a37048cd79bd753133333da5a4949d78c3ca9e3ef789913e51f8af58474c8c', 1, 2, 7, 3); 

COMMIT;

------ Pour Vérifier ----------
SELECT * FROM Competence;
SELECT * FROM Niveau;
SELECT * FROM NiveauCompetence;
SELECT * FROM Role;
SELECT * FROM Employe;

----------------------------------------------------
------------------- Procedures Stockees ------------
----------------------------------------------------

CREATE OR REPLACE PROCEDURE Authentifier (
	pLoginSaisi Employe.login%TYPE,
	pMotPasseSaisi Employe.motDePasse%TYPE,
	reponse OUT VARCHAR)
IS
-- On vérifie que le login et motDePasse saisis sont bien connus dans la table Employe
-- S'ils existent bien :
--       on retourne le rôle de l'employé concerné ('Employe' ou 'Chef de Projet') 
--       OU 'Inactif' si le client est connu mais inactif (quelque soit son rôle)
-- Sinon on retourne 'Inconnu'

	vEstActif 	Employe.estActif%TYPE;
	vNomRole 	Role.nom%TYPE;

BEGIN
	SELECT E.estActif, R.nom INTO vEstActif, vNomRole FROM Employe E, Role R
	WHERE E.idRole = R.idRole
	AND E.login = pLoginSaisi
	AND E.motDePasse = pMotPasseSaisi;
	
	IF vEstActif = 1 THEN
		reponse := vNomRole;	
	ELSE
		reponse := 'Inactif';
	END IF;
EXCEPTION
	WHEN NO_DATA_FOUND THEN	
		reponse := 'Inconnu';	
END;
/

-- Réussite avec un chef de projet
VARIABLE retour VARCHAR2;
EXECUTE Authentifier('Laurence', 'LR', :retour);
PRINT retour;

-- Réussite avec un employe
VARIABLE retour VARCHAR2;
EXECUTE Authentifier('Laurent', 'LN', :retour);
PRINT retour;

-- Echec 'Inconnu'
VARIABLE retour VARCHAR2;
EXECUTE Authentifier('Denis', 'DT', :retour);
PRINT retour;

-- Echec 'Inconnu' à cause du mot de passe
VARIABLE retour VARCHAR2;
EXECUTE Authentifier('Olivier', 'DT', :retour);
PRINT retour;

-- Echec 'Inactif' 
VARIABLE retour VARCHAR2;
EXECUTE Authentifier('Florence', 'FS', :retour);
PRINT retour;
