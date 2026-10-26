"use client";

import { createChallenge, modifyChallenge } from "@/actions/challenge-actions";
import { FormRootError } from "@/components/FormRootError";
import { Button } from "@/components/ui/button";
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { getFormErrorMessage } from "@/lib/form-utils";
import {
  challengeDataFormSchema,
  TChallengeDataFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import Link from "next/link";
import { redirect } from "next/navigation";
import { Controller, useForm } from "react-hook-form";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "./ui/card";

type Props =
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

export function ChallengeForm(props: Props) {
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
    <Card>
      <CardHeader>
        <CardTitle>
          {props.action === "CREATE" ? "Create a challenge" : "Edit challenge"}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit(onSubmit)} id="challenge-form">
          <FieldGroup>
            <Controller
              name="name"
              control={control}
              render={({ field, fieldState }) => (
                <Field>
                  <FieldLabel htmlFor={field.name}>Challenge Name</FieldLabel>
                  <Input {...field} id={field.name} placeholder="75 Hard" />
                  {fieldState.error && (
                    <FieldError errors={[fieldState.error]} />
                  )}
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
                      field.onChange(
                        digitsOnly === "" ? "" : Number(digitsOnly)
                      );
                    }}
                  />
                  {fieldState.error && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />
          </FieldGroup>
        </form>
      </CardContent>
      <CardFooter className="gap-x-2">
        <Button type="submit" form="challenge-form">
          {props.action === "CREATE" ? "Create" : "Save"}
        </Button>
        {props.action === "EDIT" && (
          <Button
            variant="outline"
            render={<Link href={`/challenges/${props.challengeId}`} />}
            nativeButton={false}
          >
            Cancel
          </Button>
        )}
        <FormRootError formState={formState} />
      </CardFooter>
    </Card>
  );
}
