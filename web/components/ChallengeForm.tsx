"use client";

import { createChallenge, modifyChallenge } from "@/actions/challenge-actions";
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

type ChallengeFormProps =
  | {
      action: "EDIT";
      defaultValues: TCreateChallengeFormSchema; // TODO: Create one type for create/modify challenge
      challengeId: number;
    }
  | {
      action: "CREATE";
      defaultValues?: never;
      challengeId?: never;
    };

export default function ChallengeForm(props: ChallengeFormProps) {
  const { control, handleSubmit, setError, formState } =
    useForm<TCreateChallengeFormSchema>({
      resolver: zodResolver(createChallengeFormSchema),
      defaultValues: props.defaultValues ?? { name: "", durationDays: 0 },
    });

  const handleCreate = async (formData: TCreateChallengeFormSchema) => {
    const res = await createChallenge(formData);
    if (res.success) {
      redirect(`/challenges/${res.data?.id}`);
    }

    setError("root", { message: res.error.detail });
  };

  const handleModify = async (
    formData: TCreateChallengeFormSchema,
    challengeId: number
  ) => {
    const res = await modifyChallenge(formData, challengeId);
    if (res.success) {
      redirect(`/challenges/${challengeId}`);
    }

    setError("root", { message: res.error.detail });
  };

  const onSubmit = async (formData: TCreateChallengeFormSchema) => {
    if (props.action === "CREATE") {
      handleCreate(formData);
    } else {
      handleModify(formData, props.challengeId);
    }
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
      <Button type="submit">
        {props.action === "CREATE" ? "Create" : "Save"}
      </Button>
      <FormRootError formState={formState} />
    </form>
  );
}
