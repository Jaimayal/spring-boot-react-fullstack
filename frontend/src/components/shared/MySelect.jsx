import { useField } from "formik"
import { Alert, AlertIcon, FormLabel, Select } from "@chakra-ui/react"

export default function MySelect({ label, ...props }) {
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
