% Reconnaissance vocale par DTW

vocabulaire={'arretetoi', 'atterrissage', 'avance', 'decollage', 'droite', 'etatdurgence', 'faisunflip', 'gauche', 'plusbas', 'plushaut', 'recule', 'tournedroite', 'tournegauche'};
chemin='dronevolant_nonbruite/' ;
nbmots=length(vocabulaire) ;

% Lecture des fichiers audio
% ref
%reference = {'M02','M03','M04', 'M05','M06','M07','M08', 'M09', 'M10', 'M11', 'M12', 'M13'} ;
reference={'M01'};


% hyp
hypotheses={'M03','F01'};
%hypotheses={'M02','M03','M04', 'M05','M06','M07','M08', 'M09', 'M10', 'M11', 'M12', 'M13'};
%hypotheses={'F02'};
%hypotheses={'F02','F03','F04', 'F05'};



for nolocuteur = 1:length(hypotheses)
    
    locuteur = hypotheses{nolocuteur};
    for mot=1:nbmots
        nomfichier = [ chemin, locuteur, '_', vocabulaire{mot}, '.wav' ] ;
        HYP{mot} = parametrisation(nomfichier);
    end
    
    
    % calcul des distances et remplissage de la matrice de confusion
    for ref=1:length(reference)
        MatriceConfusion=zeros(nbmots);
        locRef = reference{ref};
        temp = zeros(nbmots);
        for mot=1:nbmots
            nomfichier = [ chemin, locRef, '_', vocabulaire{mot}, '.wav' ] ;
            REF{mot} = parametrisation(nomfichier);
        end
        for mot1=1:nbmots
        
            for mot2=1:nbmots
                distance = CalculDistance(REF{mot2},HYP{mot1},0);

                [d,g,w]=CalculDistanceDTW(REF{mot2},HYP{mot1},distance);

                 temp(mot2)=d;
                 
                 imagesc(w >= 0)


            end  

            min = Inf;
            indiceMin = Inf;

            for mot2=1:nbmots
                if (min>temp(mot2))
                    min=temp(mot2);
                    indiceMin=mot2;
                end
            end
            MatriceConfusion(mot1,indiceMin)=1;
        end
        disp(MatriceConfusion)
        
        nbMotsReco=0;
        
        for mot2 = 1:nbmots
              if (MatriceConfusion(mot2,mot2)==1)
                  nbMotsReco=nbMotsReco+1;
              end
        end
        
        tauxreco = (nbMotsReco/(nbmots));

        disp(['Taux de reconnaissance : ', locuteur,'-', locRef,' ' ,num2str(tauxreco*100), '%']);
        
    end
end
%vocabulaire






