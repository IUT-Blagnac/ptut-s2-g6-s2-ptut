DROP TABLE Client;
DROP TABLE Projet;
DROP SEQUENCE seq_id_client;
DROP SEQUENCE seq_id_projet;


CREATE TABLE Projet(
    idNom NUMBER,
    nom VARCHAR(30),
    descriptions VARCHAR(300),
    dateDebut DATE,
    dateFinEstimee DATE,
    dateFinReel DATE,
    estActif NUMBER(1),
	CONSTRAINT PK_Projet PRIMARY KEY (idNom),
	CONSTRAINT NN_Projet_nom CHECK (nom IS NOT NULL)
);

CREATE TABLE Client(
    idNomCli NUMBER,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    entreprise VARCHAR(50),
    email VARCHAR(60),
    telephone VARCHAR(10),
    estActif NUMBER(1),
	idNom NUMBER,
	CONSTRAINT PK_Client PRIMARY KEY (idNomCli),
	CONSTRAINT NN_Client_nom CHECK (nom IS NOT NULL),
	CONSTRAINT NN_Client_prenom CHECK (prenom IS NOT NULL),
	CONSTRAINT NN_Client_email CHECK (email IS NOT NULL),
	CONSTRAINT NN_Client_telephone CHECK (telephone IS NOT NULL),
	CONSTRAINT FK_Client_idNom FOREIGN KEY (idNom) REFERENCES Projet(idNom)
);

CREATE SEQUENCE seq_id_projet MINVALUE 1 MAXVALUE 999999999999 START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_id_client MINVALUE 1 MAXVALUE 999999999999 START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE PROCEDURE Create_Projet (
	pNom Projet.nom%TYPE,
	pDescription Projet.descriptions%TYPE,
	pDateDebut Projet.dateDebut%TYPE,
	pDateFinEstimee Projet.dateFinEstimee%TYPE,
	pEstActif Projet.estActif%TYPE,
	retour OUT VARCHAR)
IS
	nom_null EXCEPTION;
	PRAGMA EXCEPTION_INIT(nom_null, -2290);
BEGIN
	INSERT INTO Projet(idNom, nom, descriptions, dateDebut, dateFinEstimee, estActif)
	VALUES(seq_id_projet.NEXTVAL, pNom, pDescription, pDateDebut, pDateFinEstimee, pEstActif);
	retour := 'Ajoutée'
EXCEPTION
	WHEN nom_null THEN
		retour := 'Nom de projet null';
END;
/

CREATE OR REPLACE PROCEDURE Create_Client (
	pNom Client.nom%TYPE,
	pPrenom Client.prenom%TYPE,
	pEntreprise Client.entreprise%TYPE,
	pEmail Client.email%TYPE,
	pTelephone Client.telephone%TYPE,
	pEstActif Client.estActif%TYPE,
	pIdNom Clien.idNom%TYPE,
	retour OUT VARCHAR)
IS 
	vEstActif Client.estActif%TYPE
	erreur_checkkey EXCEPTION;
	client_existant EXCEPTION;
	email_prise EXCEPTION;
	tel_prit EXCEPTION;
	projet_not_exist EXCEPTION;
	PRAGMA EXCEPTION_INIT(projet_not_exist, -2291);
	PRAGMA EXCEPTION_INIT(erreur_checkkey, -2290);
	n NUMBER;
	m NUMBER;
	l NUMBER;
BEGIN
	SELECT count(*) INTO n FROM Client WHERE nom = pNom AND prenom = pPrenom;
	SELECT count(*) INTO m FROM Client WHERE email = pEmail;
	SELECT count(*) INTO l FROM Client WHERE telephone = pTelephone;
	IF (n > 0 ) THEN
		RAISE client_existant;
	END IF;
	IF (m > 0  ) THEN
		RAISE email_prise;
	END IF;
	IF (l > 0  ) THEN
		RAISE tel_prit;
	END IF;
	INSERT INTO Client ( idNomCli, nom, prenom, entreprise, email, telephone, estActif, idNom) 
	VALUES (seq_id_client.NEXTVAL, pNom, pPrenom, pEntreprise, pEmail, pTelephone, pEstActif, pIdNom);
	retour := 'Ajoutée'
	COMMIT;
EXCEPTION
	WHEN projet_not_exist THEN 
		ROLLBACK;
		retour := 'Le projet n''existe pas';
	WHEN erreur_checkkey THEN
		ROLLBACK;
		retour := 'Erreur parametre null';
	WHEN client_existant THEN
		ROLLBACK;
		retour := 'Client deja existant';
	WHEN email_prise THEN
		ROLLBACK;
		retour := 'Email deja prit';
	WHEN tel_prit THEN
		ROLLBACK;
		retour := 'Numéro de téléphone déjà prit';
	WHEN OTHERS THEN
		ROLLBACK;
		DBMS_OUTPUT.PUT_LINE (SQLERRM);
		retour := 'Erreur inconnu';
END;
/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	