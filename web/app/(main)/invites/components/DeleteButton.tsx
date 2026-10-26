"use client";

import { deleteInvite } from "@/actions/invites";
import { Button } from "@/components/ui/button";
import { toastProblemDetail } from "@/lib/toast-utils";
import { Trash } from "lucide-react";

type Props = {
  inviteId: number;
};

export function DeleteButton({ inviteId }: Props) {
  const handleClick = async () => {
    const { error } = await deleteInvite(inviteId);

    if (error) {
      toastProblemDetail(error);
    }
  };

  return (
    <Button variant="ghost" onClick={handleClick}>
      <Trash />
    </Button>
  );
}
