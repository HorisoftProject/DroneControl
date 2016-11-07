
function [D,g,cheminMin] = CalculDistanceDTW(sequence1, sequence2,distance)
  w0 = 1;
  w1 = 2;
  w2 = 1;
  g(1,1) = 0;
  I = length(sequence1);
  J = length(sequence2);
  cheminMin = zeros(I+1, J+1);
  
   g = zeros(I+1, J+1);
  for j = 2 : J+1
    g(1,j) = Inf;
  end
 
 
  for i = 2 : I+1
    g(i,1) = Inf;
    for j = 2 : J+1
      %recherche du chemin minimal
      %dist = abs(sequence1(i-1,j-1)-sequence2(i-1,j-1));
      d1 = g(i-1,j) + w0*distance(i-1,j-1);
      d2 = g(i-1,j-1) + w1*distance(i-1,j-1);
      d3 = g(i,j-1) + w2*distance(i-1,j-1);
      g(i,j) = minimum(d1,d2,d3);
    end
  end
  D = g(I,J)/(I+J);
  i=I+1;
  j=J+1;
 
 
cheminMin(I+1,J+1) = g(I+1,J-1);

matriceAffichage = zeros(I+1, J+1);
matriceAffichage(1,1) = 1 ;
matriceAffichage(I+1,J+1)=1;
 
while i>=2 || j>=2
 
        a=g(i-1,j);
        b=g(i-1,j-1);
        c=g(i,j-1);
       
        if a < b
            if a <= c
                cheminMin(i-1,j)=a;
                matriceAffichage(i-1,j) = 1 ;
                i=i-1;
               
            end
        end
        if c < b
            if c < a
                cheminMin(i,j-1)=c;
                matriceAffichage(i,j-1) = 1 ;
                j=j-1;
            end
        end
           
        if b <= a && b <= c
            cheminMin(i-1,j-1)=b;
            matriceAffichage(i-1,j-1) = 1 ;
            i=i-1;
            j=j-1;
        end
     
 
end
matriceAffichage;
imagesc(matriceAffichage)
end
 
 %calcul le minimum de trois variables
function d = minimum(x,y,z)
    d = min(x,y);
    d = min(d,z);
end
 