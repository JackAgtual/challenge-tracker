"use client";

import { createGoal } from "@/actions/goal-definition";
import { Button } from "@/components/ui/button";
import { Field, FieldLabel, FieldError } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import {
  createGoalFormSchema,
  TCreateGoalFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";

type CreateGoalFormProps = {
  challengeId: number;
};

export default function CreateGoalForm({ challengeId }: CreateGoalFormProps) {
  const { control, handleSubmit, reset, setError, formState } =
    useForm<TCreateGoalFormSchema>({
      resolver: zodResolver(createGoalFormSchema),
      defaultValues: {
        name: "",
      },
    });

  const onSubmit = async (formData: TCreateGoalFormSchema) => {
    const res = await createGoal(challengeId, formData);

    if (res.success) {
      reset();
      return;
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
            <FieldLabel htmlFor={field.name}>Add a goal</FieldLabel>
            <Input {...field} id={field.name} />
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Button type="submit">Add</Button>
      {formState.errors.root && (
        <>
          <p className="text-red-600">
            Something went wrong: {formState.errors.root?.message}
          </p>
          <p className="text-red-600">Try again</p>
        </>
      )}
    </form>
  );
}
