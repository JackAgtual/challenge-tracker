"use client";

import { createChallenge } from "@/actions/create-challenge";
import FormRootError from "@/components/FormRootError";
import { Button } from "@/components/ui/button";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import {
  createChallengeFormSchema,
  TCreateChallengeFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect } from "next/navigation";
import { Controller, useForm } from "react-hook-form";

export default function CreateChallengeForm() {
  const { control, handleSubmit, setError, formState } =
    useForm<TCreateChallengeFormSchema>({
      resolver: zodResolver(createChallengeFormSchema),
      defaultValues: {
        name: "",
        durationDays: 0,
      },
    });

  const onSubmit = async (formData: TCreateChallengeFormSchema) => {
    const res = await createChallenge(formData);
    if (res.success) {
      redirect(`/challenges/${res.data?.id}`);
    }

    setError("root", { message: res.error.detail });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Controller
        name="name"
        control={control}
        render={({ field, fieldState }) => (
          <Field>
            <FieldLabel htmlFor={field.name}>Challenge Name</FieldLabel>
            <Input {...field} id={field.name} placeholder="75 Hard" />
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Controller
        name="durationDays"
        control={control}
        render={({ field, fieldState }) => (
          <Field>
            <FieldLabel htmlFor={field.name}>
              Challenge Duration (days)
            </FieldLabel>
            <Input
              {...field}
              id={field.name}
              placeholder="75"
              type="text"
              onChange={(e) => {
                const digitsOnly = e.target.value.replace(/[^0-9]/g, "");
                field.onChange(digitsOnly === "" ? "" : Number(digitsOnly));
              }}
            />
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Button type="submit">Create</Button>
      <FormRootError formState={formState} />
    </form>
  );
}
