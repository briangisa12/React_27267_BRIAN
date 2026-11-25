import React, { useEffect, useState } from 'react'
import axios from 'axios'
import LibraryButton from './LibraryButton';

const API = "http://localhost:8083/api/office";

function OfficeList() {
    const [offices, setOffices] = useState([]);
    const [search, setSearch] = useState('');

    async function loadOffices() {
        try {
            const response = await axios.get(API);
            setOffices(response.data);
        } catch {
            alert("Could not load offices");
        }
    }

    useEffect(() => { loadOffices(); }, []);

    async function deleteOffice(office) {
        if (!window.confirm("Delete office " + office.name + "?")) return;
        try {
            await axios.delete(`${API}/${office.id}`);
            setOffices(offices.filter(o => o.id !== office.id));
        } catch {
            alert("Deleting office failed");
        }
    }

    // shortcut: filters in the browser, move to a backend search endpoint if the list gets large
    const text = search.toLowerCase();
    const filtered = offices.filter(o =>
        (o.name ?? '').toLowerCase().includes(text) || String(o.officeNumber).includes(text)
    );

    return (
        <div>
            <h1>Offices Page</h1>
            <label>Search: </label>
            <input type='text' value={search} placeholder='Office name or number'
                onChange={e => setSearch(e.target.value)} />
            <table border="1" cellPadding="6" style={{ margin: "10px auto", borderCollapse: "collapse" }}>
                <thead>
                    <tr>
                        <th>id</th>
                        <th>officeNumber</th>
                        <th>officeName</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.map((office, index) => (
                        <tr key={office.id}>
                            <td>{index + 1}</td>
                            <td>{office.officeNumber}</td>
                            <td>{office.name}</td>
                            <td>
                                <LibraryButton buttonLabel="Delete" onClickButton={() => deleteOffice(office)} />
                            </td>
                        </tr>
                    ))}
                    {filtered.length === 0 && <tr><td colSpan="4">No offices found</td></tr>}
                </tbody>
            </table>
        </div>
    )
}

export default OfficeList
