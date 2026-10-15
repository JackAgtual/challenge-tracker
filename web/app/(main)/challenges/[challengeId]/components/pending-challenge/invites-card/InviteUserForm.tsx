"use client";

import { inviteUser } from "@/actions/invites";
import { searchUsers } from "@/actions/user-search";
import { Button } from "@/components/ui/button";
import {
  Combobox,
  ComboboxContent,
  ComboboxEmpty,
  ComboboxInput,
  ComboboxItem,
  ComboboxList,
} from "@/components/ui/combobox";
import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { useDebounce } from "@/hooks/use-debounce";
import { getFormErrorMessage } from "@/lib/form-utils";
import { getDisplayNameFromUser } from "@/lib/name-utils";
import { toastProblemDetail } from "@/lib/toast-utils";
import { components } from "@/types/api";
import {
  inviteUserFormSchema,
  TInviteUserFormSchema,
} from "@/types/form-types";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { Controller, useForm } from "react-hook-form";

type InviteUserProps = {
  challengeId: number;
};

export default function InviteUserForm({ challengeId }: InviteUserProps) {
  const [query, setQuery] = useDebounce("");
  const [queryRes, setQueryRes] = useState<
    components["schemas"]["UserResponse"][]
  >([]);
  const [comboKey, setComboKey] = useState(1); // Used to clear combobox input after submit

  useEffect(() => {
    async function getUsers() {
      if (query.trim() === "") {
        setQueryRes([]);
        return;
      }

      const { success, data: users, error } = await searchUsers(query);

      if (!success) {
        toastProblemDetail(error);
        return;
      }

      setQueryRes(users);
    }
    getUsers();
  }, [query]);

  const { control, handleSubmit, setError } = useForm<TInviteUserFormSchema>({
    resolver: zodResolver(inviteUserFormSchema),
    defaultValues: { username: "" },
  });

  async function onSubmit(formData: TInviteUserFormSchema) {
    const { success, error } = await inviteUser(challengeId, formData);

    if (!success) {
      setError("username", getFormErrorMessage(error));
      return;
    }

    setComboKey((prev) => prev + 1);
    setQueryRes([]);
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Controller
        control={control}
        name="username"
        render={({ field, fieldState }) => (
          <Field>
            <FieldLabel>Username</FieldLabel>
            <Combobox
              key={comboKey}
              filter={null}
              items={queryRes}
              itemToStringValue={getDisplayNameFromUser}
              onValueChange={(val) => {
                field.onChange(val);
              }}
              onInputValueChange={(text) => {
                setQuery(text);
              }}
            >
              <ComboboxInput id={field.name} placeholder="select user" />
              <ComboboxContent>
                <ComboboxEmpty>No users found</ComboboxEmpty>
                <ComboboxList>
                  {(user) => (
                    <ComboboxItem key={user.username} value={user.username}>
                      {getDisplayNameFromUser(user)}
                    </ComboboxItem>
                  )}
                </ComboboxList>
              </ComboboxContent>
            </Combobox>
            {fieldState.error && <FieldError errors={[fieldState.error]} />}
          </Field>
        )}
      />
      <Button type="submit">Invite</Button>
    </form>
  );
}
