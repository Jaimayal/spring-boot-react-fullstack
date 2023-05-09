import { useContext, createContext, useState, useEffect } from "react"
import { login } from "../services/AuthClient.js"
import jwtDecode from "jwt-decode"

const AuthContext = createContext({})

function AuthProvider({ children }) {
    const [customer, setCustomer] = useState(null)

    useEffect(() => {
        const token = localStorage.getItem("access-token")
        if (!token) {
            return
        }
        const { sub, scopes } = jwtDecode(token)
        setCustomer({
            email: sub,
            roles: scopes,
        })
    }, [])

    const loginAndUpdateLocalStorage = async (authRequest) => {
        return new Promise((resolve, reject) => {
            login(authRequest)
                .then((response) => {
                    const token = response.headers["authorization"]
                    localStorage.setItem("access-token", token)
                    const { sub, scopes } = jwtDecode(token)
                    console.log(sub)
                    console.log(scopes)
                    setCustomer({
                        email: sub,
                        roles: scopes,
                    })
                    resolve(response)
                })
                .catch((err) => reject(err))
        })
    }

    const logoutAndClearLocalStorage = () => {
        localStorage.removeItem("access-token")
        setCustomer(null)
    }

    const isUserLoggedIn = () => {
        const token = localStorage.getItem("access-token")
        if (!token) {
            return false
        }

        const { exp: expirationTime } = jwtDecode(token)
        if (Date.now() > new Date(expirationTime * 1000)) {
            logoutAndClearLocalStorage()
            return false
        }

        return true
    }

    return (
        <AuthContext.Provider
            value={{
                customer,
                loginAndUpdateLocalStorage,
                logoutAndClearLocalStorage,
                isUserLoggedIn,
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)
export default AuthProvider
