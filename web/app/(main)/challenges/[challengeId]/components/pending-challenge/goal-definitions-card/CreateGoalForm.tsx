"use client";

import { createGoal } from "@/actions/goal-definition";
import { FormRootError } from "@/components/FormRootError";
import { Button } from "@/components/ui/button";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { getFormErrorMessage } from "@/lib/form-utils";
import {
  createGoalFormSchema,
  TCreateGoalFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";

type Props = {
  challengeId: number;
};

export function CreateGoalForm({ challengeId }: Props) {
  const { control, handleSubmit, reset, setError, formState } =
    useForm<TCreateGoalFormSchema>({
      resolver: zodResolver(createGoalFormSchema),
      defaultValues: {
        name: "",
      },
    });

  const onSubmit = async (formData: TCreateGoalFormSchema) => {
    const { success, error } = await createGoal(challengeId, formData);

    if (success) {
      reset();
      return;
    }

    setError("root", getFormErrorMessage(error));
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} aria-label="Create goal definition">
      <Controller
        name="name"
        control={control}
        render={({ field, fieldState }) => (
          <Field>
            <FieldLabel htmlFor={field.name}>Add a goal</FieldLabel>
            <Input {...field} id={field.name} />
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Button type="submit">Add</Button>
      <FormRootError formState={formState} />
    </form>
  );
}
