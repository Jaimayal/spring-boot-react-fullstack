import { useNavigate } from "react-router-dom"
import { useEffect, useState } from "react"
import { useAuth } from "../../context/AuthContext.jsx"

export default function ProtectedRouteProvider({ children }) {
    const { isUserLoggedIn } = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        if (!isUserLoggedIn()) {
            console.log("GOING TO /")
            navigate("/")
        }
    })

    return isUserLoggedIn() ? (
        children
    ) : (
        <>
            <p>You're logged out</p>
        </>
    )
}
