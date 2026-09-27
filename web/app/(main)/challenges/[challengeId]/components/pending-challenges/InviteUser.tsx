"use client";

import { inviteUser } from "@/actions/invites";
import { Button } from "@/components/ui/button";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import {
  inviteUserFormSchema,
  TInviteUserFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";

type InviteUserProps = {
  challengeId: number;
};

export default function InviteUser({ challengeId }: InviteUserProps) {
  const { control, handleSubmit, setError, formState } =
    useForm<TInviteUserFormSchema>({
      resolver: zodResolver(inviteUserFormSchema),
      defaultValues: { username: "" },
    });

  async function onSubmit(formData: TInviteUserFormSchema) {
    const { success, error } = await inviteUser(challengeId, formData);

    if (success) {
      return;
    }

    console.log(error);
    setError("username", { message: error?.detail });
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Controller
        control={control}
        name="username"
        render={({ field, fieldState }) => (
          <Field>
            <FieldLabel>Username</FieldLabel>
            <Input {...field} id={field.name} placeholder="75 Hard" />
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Button type="submit">Invite</Button>
    </form>
  );
}
