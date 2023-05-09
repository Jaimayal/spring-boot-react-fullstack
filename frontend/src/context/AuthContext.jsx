import { useContext, createContext } from "react"
import { login } from "../services/AuthClient.js"

const AuthContext = createContext({})

function AuthProvider({ children }) {
    const loginAndUpdateLocalStorage = async (authRequest) => {
        return new Promise((resolve, reject) => {
            login(authRequest)
                .then((response) => {
                    const location = response.headers["location"]
                    const token = response.headers["authorization"]
                    localStorage.setItem("access-token", token)
                    localStorage.setItem("user-location", location)
                    resolve(response)
                })
                .catch((err) => reject(err))
        })
    }

    return (
        <AuthContext.Provider
            value={{
                loginAndUpdateLocalStorage,
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)
export default AuthProvider
