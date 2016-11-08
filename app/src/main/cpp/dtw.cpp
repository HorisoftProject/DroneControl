/*******************************************************************************
 *
 * Drone control through voice recognition -- PC to drone communication
 * Team GYTAM, feb. 2016
 *
 *
 ******************************************************************************/

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h> // for memcmp
#include <stdint.h> // for int16_t and int32_t
#include <math.h>
#include <iostream>
#include "dtw.h"

#define NB_MOTS 13

using namespace std;


/**
* Dtw function that given two matrix of cep coefficient computes distance
* between those two signals.
*  @param n_ck      Dimension of unknow signal
*  @param n_cunk    Dimension of know signal
*  @param dim_mfcc  Size of nfcc decompostion base
*  @param c_k       Matrix of know signal
*  @param c_unk     Matrix of unknow signal
*  @return Distance between the two signals
*/

float dtw(int n_ck, int n_cunk, int dim_mfcc, float* c_k, float* c_unk) {

/* à compléter */
    
    
}



int main() {

	string vocabulaire[NB_MOTS]= {"arretetoi", "atterrissage", "avance", "decollage", "droite", "etatdurgence", "faisunflip", "gauche", "plusbas", "plushaut", "recule", "tournedroite", "tournegauche"};

	string chemin="dronevolant_nonbruite/";

	// Lecture des fichiers audio
	//ref
	//reference = {"M02","M03","M04", "M05","M06","M07","M08", "M09", "M10", "M11", "M12", "M13"} ;
	string reference [1]={"M01"};

	// hyp
	//string hypotheses [11]={"M02","M03","M04", "M05","M06","M07","M08", "M09", "M10", "M11", "M12", "M13"};
	//string hypotheses[1]={"F02"};
	//string hypotheses[4]={"F02","F03","F04", "F05"};
	string hypotheses[2]={"M03","F01"};



	int sizeHypotheses = sizeof(hypotheses)/sizeof(hypotheses[1]);

	for (int nolocuteur=0;nolocuteur<sizeHypotheses;nolocuteur++) {  
		string locuteur = hypotheses[nolocuteur];

		for (int mot=0; mot<NB_MOTS ; mot++ ) {
			string nomfichier = chemin + locuteur + "_" + vocabulaire[mot] + ".wav";
		}
	}

	

}