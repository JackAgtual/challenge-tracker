"use client";

import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { Field, FieldLabel } from "@/components/ui/field";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { components } from "@/types/api";
import { TSetupAccountFormSchema } from "@/types/form-types";
import { cn } from "cn";
import { useState } from "react";
import { Control, Controller } from "react-hook-form";

type AvatarSelectionProps = {
  control: Control<TSetupAccountFormSchema>;
  availableAvatars: components["schemas"]["AvatarResponse"][];
  defaultAvatar: string | undefined;
};
export default function AvatarSelection({
  control,
  availableAvatars,
  defaultAvatar,
}: AvatarSelectionProps) {
  const [selectionMade, setSelectionMade] = useState(false);

  return (
    <Controller
      name="avatarSrc"
      control={control}
      render={({ field }) => (
        <Field>
          <FieldLabel htmlFor={field.name}>Profile icon</FieldLabel>
          <RadioGroup
            value={field.value ?? ""}
            onValueChange={(val) => {
              setSelectionMade(true);
              field.onChange(val);
            }}
            onBlur={field.onBlur}
            className="flex flex-wrap"
          >
            {availableAvatars.map(({ src }) => {
              const isSelected = selectionMade
                ? field.value === src
                : defaultAvatar === src;

              return (
                <div key={src}>
                  <RadioGroupItem value={src} id={src} className="sr-only" />
                  <Label htmlFor={src}>
                    <Avatar
                      data-testid={src}
                      className={cn(
                        "size-14 border-4 border-transparent",
                        isSelected ? "border-black" : "border-transparent"
                      )}
                    >
                      <AvatarImage src={src} />
                    </Avatar>
                  </Label>
                </div>
              );
            })}
          </RadioGroup>
        </Field>
      )}
    />
  );
}
