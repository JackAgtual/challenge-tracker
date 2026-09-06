"use client";

import { accountSetup } from "@/actions/account-setup";
import { Button } from "@/components/ui/button";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
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
    const res = await accountSetup(formData);

    if (res.success) {
      redirect("/dashboard");
    }
    setError("root", { message: res.error.detail });
  };

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <Controller
          name="firstName"
          control={control}
          render={({ field, fieldState }) => (
            <Field>
              <FieldLabel htmlFor={field.name}>First Name</FieldLabel>
              <Input {...field} id={field.name} placeholder="John" />
              {fieldState.error && <FieldError errors={[fieldState.error]} />}
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
              {fieldState.error && <FieldError errors={[fieldState.error]} />}
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
              {fieldState.error && <FieldError errors={[fieldState.error]} />}
            </Field>
          )}
        />
        <Button type="submit">Submit</Button>
        {formState.errors.root && (
          <>
            <p className="text-red-600">
              Something went wrong: {formState.errors.root?.message}
            </p>
            <p className="text-red-600">Try again</p>
          </>
        )}
      </form>
    </>
  );
}
