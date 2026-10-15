"use client";

import { createChallenge, modifyChallenge } from "@/actions/challenge-actions";
import FormRootError from "@/components/FormRootError";
import { Button } from "@/components/ui/button";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { getFormErrorMessage } from "@/lib/form-utils";
import {
  challengeDataFormSchema,
  TChallengeDataFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect } from "next/navigation";
import { Controller, useForm } from "react-hook-form";

type ChallengeFormProps =
  | {
      action: "EDIT";
      defaultValues: TChallengeDataFormSchema;
      challengeId: number;
    }
  | {
      action: "CREATE";
      defaultValues?: never;
      challengeId?: never;
    };

export default function ChallengeForm(props: ChallengeFormProps) {
  const { control, handleSubmit, setError, formState } =
    useForm<TChallengeDataFormSchema>({
      resolver: zodResolver(challengeDataFormSchema),
      defaultValues: props.defaultValues ?? { name: "", durationDays: 0 },
    });

  const handleCreate = async (formData: TChallengeDataFormSchema) => {
    const { success, data, error } = await createChallenge(formData);
    if (success) {
      redirect(`/challenges/${data.id}`);
    }

    setError("root", getFormErrorMessage(error));
  };

  const handleModify = async (
    formData: TChallengeDataFormSchema,
    challengeId: number
  ) => {
    const { success, error } = await modifyChallenge(formData, challengeId);
    if (success) {
      redirect(`/challenges/${challengeId}`);
    }

    setError("root", getFormErrorMessage(error));
  };

  const onSubmit = async (formData: TChallengeDataFormSchema) => {
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
