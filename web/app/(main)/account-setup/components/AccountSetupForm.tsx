"use client";

import { accountSetup } from "@/actions/account-setup";
import FormRootError from "@/components/FormRootError";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { getFormErrorMessage } from "@/lib/form-utils";
import {
  TSetupAccountFormSchema,
  setupAccountFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect } from "next/navigation";
import { Controller, useForm } from "react-hook-form";

type AccountSetupFormProps = {
  defaultValues: TSetupAccountFormSchema;
};

export default function AccountSetupForm({
  defaultValues,
}: AccountSetupFormProps) {
  const { control, handleSubmit, setError, formState } =
    useForm<TSetupAccountFormSchema>({
      resolver: zodResolver(setupAccountFormSchema),
      defaultValues,
    });

  const onSubmit = async (formData: TSetupAccountFormSchema) => {
    const { success, error } = await accountSetup(formData);

    if (success) {
      redirect("/dashboard");
    }
    setError("root", getFormErrorMessage(error));
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Finish setting up your account</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit(onSubmit)} id="account-form">
          <FieldGroup>
            <Controller
              name="firstName"
              control={control}
              render={({ field, fieldState }) => (
                <Field>
                  <FieldLabel htmlFor={field.name}>First Name</FieldLabel>
                  <Input {...field} id={field.name} placeholder="John" />
                  {fieldState.error && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />
            <Controller
              name="lastName"
              control={control}
              render={({ field, fieldState }) => (
                <Field>
                  <FieldLabel htmlFor={field.name}>Last Name</FieldLabel>
                  <Input {...field} id={field.name} placeholder="Smith" />
                  {fieldState.error && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />{" "}
            <Controller
              name="username"
              control={control}
              render={({ field, fieldState }) => (
                <Field>
                  <FieldLabel htmlFor={field.name}>Username</FieldLabel>
                  <Input {...field} id={field.name} placeholder="jsmith12" />
                  {fieldState.error && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />
          </FieldGroup>
        </form>
      </CardContent>
      <CardFooter>
        <Button type="submit" form="account-form">
          Submit
        </Button>
        <FormRootError formState={formState} />
      </CardFooter>
    </Card>
  );
}
