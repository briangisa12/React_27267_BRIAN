import React, { useState } from 'react'
import axios from 'axios'
import LibraryButton from './LibraryButton';

function Office() {
    const [officeName, setOfficeName] = useState('');
    const [officeNumber, setOfficeNumber] = useState('');

    async function saveOffice(){
        const response = await axios.post("http://localhost:8083/api/office/save",
            {
                name: officeName,
                officeNumber: officeNumber
            }
         )
         if (response.status === 200){
            alert("Office Saved Successfully");
         }else{
            alert(" Saving Office failed");
         }
    }

  return (
    <div>
      <h1>Save OFFICE</h1>
      <label>Office Name:</label><input type = 'text' value={officeName} onChange={(e) => setOfficeName(e.target.value)}/><br/>
      <label>Office Number:</label><input type = 'text' value={officeNumber} onChange={(e) => setOfficeNumber(e.target.value)}/><br/>
      <LibraryButton buttonLabel="SAVE" onClickButton={saveOffice}/>

    </div>
  )
}

export default Office