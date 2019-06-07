#!/bin/bash

galleryCompositionality() {
  declare -a array=("Requirement2 [T= RmD" "TT  [F=  RmA5"  "Requirement4 [T= ExhibitionArea4" "RR2 [T= ArtGallery2" "RR4 [T=  ArtGallery4"  "RR5A [F= ArtGallery5")

 declare -a arr=("A"  "B" "C" "D" "E" "F")
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=10
 local min=10
 local max=70
 local foldername=$(date +%Y%m%d_%H%M%S)
 arraylength=${#array[@]}
  # Loop the config array
    


  for ((i=min; i<=max; i+=interval))
  do
    for (( j=0; j<${arraylength}; j++ ))
    do
      replace=${array[$j]}
      
      
      sed "s/${field}/${i}/g" ArtGalleryN.csp > CompositionalityCSPN.csp
      
      
      sed -i "s/${ass}/${replace}/g" CompositionalityCSPN.csp 

      filename="results/galleryCompositionality/${i}${arr[$j]}"
      
 	    time(refines --format=framed_json CompositionalityCSPN.csp ) > "${filename}_MinimizationServer.json" 2> "${filename}_MinimizationServer.txt" 
      sleep 5 &
    done 
  done 
}



gallery() {
 declare -a array=( "Requirement5A [|event(\"A\",\"BD\") |] RoomBD [F= ExhibitionArea")
 declare -a arr=( "B" )
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=10
 local min=190
 local max=200
 local foldername=$(date +%Y%m%d_%H%M%S)
 arraylength=${#array[@]}
  # Loop the config array
    


  for ((i=min; i<=max; i+=interval))
  do
  	for (( j=0; j<${arraylength}; j++ ))
  	do

     replace=${array[$j]}
      
      
      sed "s/${field}/${i}/g" ApproachOneN.csp > GalleryCSPN.csp
      
      
      sed -i  "s/${ass}/${replace}/g" GalleryCSPN.csp 

      filename="results/gallery/${i}${arr[$j]}"
       	time(refines --format=framed_json --quiet  GalleryCSPN.csp ) > "${filename}.json" 2> "${filename}.txt" 
  	done
  done
  
}

galleryNoMin() {
 declare -a array=( "Requirement5A  [|event(\"A\",\"BD\") |] RoomBD[F= ExhibitionArea")
 declare -a arr=( "B" )
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=10
 local min=10
 local max=80
 local foldername=$(date +%Y%m%d_%H%M%S)
 arraylength=${#array[@]}
  # Loop the config array
    


  for ((i=min; i<=max; i+=interval))
  do
        for (( j=0; j<${arraylength}; j++ ))
        do

     replace=${array[$j]}
      
      
      sed "s/${field}/${i}/g" ApproachOne.csp > GalleryCSP.csp
      
      
      sed -i  "s/${ass}/${replace}/g" GalleryCSP.csp 

      filename="results/galleryNoMin/${i}${arr[$j]}"
        time(refines --format=framed_json --quiet --compiler-leaf-compression=none  GalleryCSP.csp ) > "${filename}.json" 2> "${filename}.txt" 
        done
  done
  
}


numsections() {
	local -a array=("ReqA [T= Section" "ReqB [T= Section" "ReqC [T= Section" "ReqD [T= Section" "ReqE [T= Section" "ReqF [T= Section" "ReqG [T= Section" "Section :[deterministic]" "Section :[deadlock free]" "Section :[divergence free]")
	local -a arr=("A" "B" "C" "D" "E" "F" "G" "Deterministic" "Deadlock" "Divergence")
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=5
 local min=5
 local max=50
 local foldername=$(date +%Y%m%d_%H%M%S)
arraylength=${#array[@]}
  # Loop the config array
    

  for ((i=min; i<=max; i+=interval))
  do
  	for (( j=0; j<${arraylength}; j++ ))
  	do
      replace=${array[$j]}
      
     
      sed "s/${field}/${i}/g" StadiumSectionsN.csp > StadiumSectionCSPN.csp
      
      
      sed -i "s/${ass}/${replace}/g" StadiumSectionCSPN.csp 

      filename="results/numsections/${i}${arr[$j]}"
       	time(refines --format=framed_json  StadiumSectionCSPN.csp ) > "${filename}_MinimizationServer.json" 2> "${filename}_MinimizationServer.txt" 
  	done
  done  
}

sectionsize() {
	local -a array=("ReqA [T= Section" "ReqB [T= Section" "ReqC [T= Section" "ReqD [T= Section" "ReqE [T= Section" "ReqF [T= Section" "ReqG [T= Section" "Section :[deterministic]" "Section :[deadlock free]" "Section :[divergence free]")
	local -a arr=("A" "B" "C" "D" "E" "F" "G" "Deterministic" "Deadlock" "Divergence")
	 local field='%%SECTIONSIZE%%'
	 local ass='%%ASSERTION%%'
 	local interval=50
	 local min=100
 	local max=1000
	 local foldername=$(date +%Y%m%d_%H%M%S)
	arraylength=${#array[@]}
  # Loop the config array
    

  for ((i=min; i<=max; i+=interval))
  do
  	for (( j=0; j<${arraylength}; j++ ))
  	do
  	 replace=${array[$j]}
      
     
      sed "s/${field}/${i}/g" StadiumN.csp > StadiumCSPN.csp
      
     
      sed -i "s/${ass}/${replace}/g" StadiumCSPN.csp 

      filename="results/sectionsize/${i}${arr[$j]}"
       	time(refines --format=framed_json StadiumCSPN.csp ) > "${filename}_MinimizationServer.json" 2> "${filename}_MinimizationServer.txt" 
  	done
  done
}

NoMinnumsections() {
        local -a array=("ReqA [T= Section" "ReqB [T= Section" "ReqC [T= Section" "ReqD [T= Section" "ReqE [T= Section" "ReqF [T= Section" "ReqG [T= Section" "Section :[deterministic" "Section :[deadlock free]" "Section :[divergence free")
        local -a arr=("A" "B" "C" "D" "E" "F" "G" "Deterministic" "Deadlock" "Divergence")
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=5
 local min=5
 local max=30
 local foldername=$(date +%Y%m%d_%H%M%S)
arraylength=${#array[@]}
  # Loop the config array
    
  for ((i=min; i<=max; i+=interval))
  do
        for (( j=0; j<${arraylength}; j++ ))
        do
      replace=${array[$j]}
      
     
      sed "s/${field}/${i}/g" StadiumSections.csp > StadiumSectionCSPN.csp
      
      
      sed -i  "s/${ass}/${replace}/g" StadiumSectionCSPN.csp 

      filename="results/numsectionsNoMin/${i}${arr[$j]}"
        time(refines --format=framed_json --quiet --compiler-leaf-compression=none StadiumSectionCSPN.csp ) > "${filename}.json" 2> "${filename}.txt"
        done
  done  
}

NoMinsectionsize() {
   local -a array=("ReqA [T= Section" "ReqB [T= Section" "ReqC [T= Section" "ReqD [T= Section" "ReqE [T= Section" "ReqF [T= Section" "ReqG [T= Section" "Section :[deterministic]" "Section :[deadlock free]" "Section :[divergence free]")
    local -a arr=("A" "B" "C" "D" "E" "F" "G" "Deterministic" "Deadlock" "Divergence")
 local field='%%SECTIONSIZE%%'
 local ass='%%ASSERTION%%'
 local interval=20
 local min=200
 local max=250
 local foldername=$(date +%Y%m%d_%H%M%S)
arraylength=${#array[@]}
  # Loop the config array
    
  
  for ((i=min; i<=max; i+=interval))
  do
        for (( j=0; j<${arraylength}; j++ ))
        do
         replace=${array[$j]}
      
     
      sed "s/${field}/${i}/g" Stadium.csp > StadiumCSPN.csp
      
     
      sed -i "s/${ass}/${replace}/g" StadiumCSPN.csp 

      filename="results/sectionsizeNoMin/${i}${arr[$j]}"
        time(refines --format=framed_json --quiet --compiler-leaf-compression=none StadiumCSPN.csp ) > "${filename}.json" 2> "${filename}.txt" 
        done
  done
}



gallery

exit
