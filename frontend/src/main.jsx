import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App.jsx"
import "./index.css"
import { ChakraProvider, createStandaloneToast, Link } from "@chakra-ui/react"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import Authentication from "./components/login/Authentication.jsx"
import AuthProvider from "./context/AuthContext.jsx"
import ProtectedRouteProvider from "./components/shared/ProtectedRouteProvider.jsx"
import LoginForm from "./components/login/LoginForm.jsx"
import RegisterForm from "./components/login/RegisterForm.jsx"

const router = createBrowserRouter([
    // {
    //     path: "/*",
    //     element: <ProtectedRouteProvider />,
    // },
    {
        path: "/",
        element: (
            <Authentication title={"Login"}>
                <LoginForm />
                <Link
                    mt={"4vh"}
                    textAlign={"center"}
                    color={"blue.500"}
                    href={"/register"}
                >
                    Don't have an account ? Register now
                </Link>
            </Authentication>
        ),
    },
    {
        path: "/register",
        element: (
            <Authentication title={"Register"}>
                <RegisterForm />
                <Link
                    mt={"4vh"}
                    textAlign={"center"}
                    color={"blue.500"}
                    href={"/"}
                >
                    You already have an account ? Log in now
                </Link>
            </Authentication>
        ),
    },
    {
        path: "dashboard",
        element: (
            <ProtectedRouteProvider>
                <App />
            </ProtectedRouteProvider>
        ),
    },
])

const { ToastContainer } = createStandaloneToast()
ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <ChakraProvider>
            <AuthProvider>
                <RouterProvider router={router} />
            </AuthProvider>
            <ToastContainer />
        </ChakraProvider>
    </React.StrictMode>
)
