import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import Office from './component/Office'
import OfficeList from './component/OfficeList'
import LibraryLogin from './component/LibraryLogin'
import LibraryBooks from './component/LibraryBooks'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LibraryLogin/>} />
        <Route path="/office" element={<><Office/><OfficeList/></>} />
        <Route path="/books" element={<LibraryBooks/>} />
        <Route path="*" element={<LibraryLogin/>} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
