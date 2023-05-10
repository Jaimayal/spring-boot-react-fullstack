import { Form, Formik } from "formik"
import * as Yup from "yup"
import { useAuth } from "../../context/AuthContext.jsx"
import { useNavigate } from "react-router-dom"
import { errorToastNotification } from "../../services/UiNotificationProvider.js"
import MyTextInput from "../shared/MyTextInput.jsx"
import { Button, Stack } from "@chakra-ui/react"
import MySelect from "../shared/MySelect.jsx"

export default function RegisterForm() {
    const { registerAndUpdateLocalStorage } = useAuth()
    const navigate = useNavigate()
    const initialValues = {
        name: "",
        email: "",
        password: "",
        age: "",
        gender: "",
    }

    const validationSchema = Yup.object({
        name: Yup.string().max(45, "Must be at max 45 characters").required(),
        email: Yup.string().email("Must be a valid email").required(),
        password: Yup.string()
            .max(30, "Must be at max 30 characters")
            .min(8, "Must be 8 characters or longer")
            .required(),
        age: Yup.number().moreThan(18, "Must be 18 or older").required(),
        gender: Yup.string()
            .oneOf(["male", "female"], "Must be male or female")
            .required(),
    })
    const onSubmit = (values, { setSubmitting }) => {
        setSubmitting(true)
        registerAndUpdateLocalStorage({ ...values })
            .then(() => {
                navigate("/dashboard")
            })
            .catch((error) =>
                errorToastNotification(error.code, error.response.data.message)
            )
            .finally(() => setSubmitting(false))
    }

    return (
        <Formik
            validateOnMount={true}
            initialValues={initialValues}
            onSubmit={onSubmit}
            validationSchema={validationSchema}
        >
            {({ isValid, isSubmitting, dirty }) => (
                <Form>
                    <Stack spacing={"1vh"}>
                        <MyTextInput
                            label="Name"
                            name="name"
                            type="text"
                            placeholder="James"
                        />

                        <MyTextInput
                            label="Email"
                            name="email"
                            type="email"
                            placeholder="some@email.com"
                        />

                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                            placeholder="********"
                        />

                        <MyTextInput
                            label="Age"
                            name="age"
                            type="number"
                            placeholder="21"
                        />

                        <MySelect label="Gender" name="gender">
                            <option value="">Select a gender</option>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </MySelect>

                        <Button
                            type="submit"
                            isDisabled={!dirty || !isValid || isSubmitting}
                        >
                            Register
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}
