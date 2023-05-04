import { Formik, Form, useField } from "formik"
import * as Yup from "yup"
import {
    Alert,
    AlertIcon,
    Button,
    FormLabel,
    Input,
    Select,
    Stack,
} from "@chakra-ui/react"
import * as ApiClient from "../services/ApiClient.js"
import {
    successToastNotification,
    errorToastNotification,
} from "../services/UiNotificationProvider.js"

const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props)
    return (
        <>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </>
    )
}

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props)
    return (
        <>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </>
    )
}

// And now we can use these
export default function CreateCustomerForm({ fetchCustomers, onClose }) {
    return (
        <>
            <Formik
                validateOnMount={true}
                initialValues={{
                    name: "",
                    email: "",
                    age: "",
                    gender: "",
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, "Must be 15 characters or less")
                        .required("Required"),
                    email: Yup.string()
                        .email("Invalid email address")
                        .required("Required"),
                    age: Yup.number()
                        .min(18, "Must be 18 years or older")
                        .max(100, "Must be 100 years or younger")
                        .required("Required"),
                    gender: Yup.string()
                        .oneOf(["male", "female"], "Invalid Gender")
                        .required("Required"),
                })}
                onSubmit={(values, { setSubmitting }) => {
                    setSubmitting(true)
                    ApiClient.saveCustomer(values)
                        .then((_) => {
                            successToastNotification(
                                "Success",
                                "Customer created successfully"
                            )
                            fetchCustomers()
                            onClose()
                        })
                        .catch((error) =>
                            errorToastNotification(
                                error.code,
                                error.response.data.message
                            )
                        )
                        .finally(() => setSubmitting(false))
                }}
            >
                {({ isValid, isSubmitting }) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@gmail.com"
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
                                isDisabled={isSubmitting || !isValid}
                            >
                                Submit
                            </Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    )
}
