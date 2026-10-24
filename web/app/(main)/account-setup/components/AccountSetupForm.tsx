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
import { components } from "@/types/api";
import {
  TSetupAccountFormSchema,
  setupAccountFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect } from "next/navigation";
import { Controller, useForm } from "react-hook-form";
import AvatarSelection from "./AvatarSelection";

type AccountSetupFormProps = {
  defaultValues: TSetupAccountFormSchema;
  availableAvatars: components["schemas"]["AvatarResponse"][];
};

export default function AccountSetupForm({
  defaultValues,
  availableAvatars,
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
    } else {
      // Need to use else statement so tests pass
      setError("root", getFormErrorMessage(error));
    }
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
            <AvatarSelection
              control={control}
              availableAvatars={availableAvatars}
              defaultAvatar={defaultValues.avatarSrc}
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
