function distance=CalculDistance(sequence1,sequence2,type)
    
I=length(sequence1);
J=length(sequence2);
distance=zeros(I,J);

if type==1
    for i=1:I,
        for j=1:J,
            distance(i,j)=distance_adn(sequence1,sequence2,i,j);
        end
    end
    

else
    for i=1:I,
        for j=1:J,
            distance(i,j)=distance_audio(sequence1,sequence2,i,j);
            %distance=dist(sequence1,sequence2);
        end
    end
end

end



function d=distance_num(sequence1,sequence2,indicei,indicej)
  d=abs(sequence2(indicej)-sequence1(indicei));
end

function d=distance_audio(sequence1,sequence2,indicej,indicej2)
    G=sequence1(:,indicej);
    G2=sequence2(:,indicej2);
    d = sqrt(sum((G - G2) .^ 2));
end

function d=distance_adn(sequence1,sequence2,indicei,indicej)
  if sequence1(indicei)==sequence2(indicej)
    d=0;
  else
    d=1;
  end
end

