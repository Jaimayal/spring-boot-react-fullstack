import * as Yup from "yup"
import { Form, Formik } from "formik"
import MyTextInput from "../shared/MyTextInput.jsx"
import { Button, Stack } from "@chakra-ui/react"
import { useAuth } from "../../context/AuthContext.jsx"
import { errorToastNotification } from "../../services/UiNotificationProvider.js"
import { useNavigate } from "react-router-dom"

const initialValues = {
    email: "",
    password: "",
}

const validationSchema = Yup.object({
    email: Yup.string()
        .email("The email must be a valid email")
        .required("Email is required"),
    password: Yup.string()
        .max(30, "Password cannot be longer than 30 characters")
        .min(8, "Password must be 8 characters min")
        .required("Password is required"),
})

export default function LoginForm() {
    const { loginAndUpdateLocalStorage } = useAuth()
    const navigate = useNavigate()

    const onSubmit = (values, { setSubmitting }) => {
        setSubmitting(true)
        loginAndUpdateLocalStorage({ ...values })
            .then(() => {
                navigate("/dashboard")
            })
            .catch((error) => {
                errorToastNotification(error.code, error.response.data.message)
            })
            .finally(() => setSubmitting(false))
    }

    return (
        <Formik
            validateOnMount={true}
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
        >
            {({ isSubmitting, dirty, isValid }) => (
                <Form>
                    <Stack spacing={"1vh"}>
                        <MyTextInput
                            label={"Email"}
                            name={"email"}
                            type={"text"}
                            placeholder={"some@emaill.com"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"********"}
                        />
                        <Button
                            type={"submit"}
                            disabled={!dirty || !isValid || isSubmitting}
                            width={"full"}
                        >
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}
