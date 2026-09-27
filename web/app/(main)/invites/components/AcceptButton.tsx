"use client";

import { Button } from "@/components/ui/button";
import { acceptInvite } from "@/actions/invites";
import { redirect } from "next/navigation";
import { toastGenericError } from "@/lib/toast-utils";

type AcceptButtonProps = {
  inviteId: number;
  challengeId: number;
};

export default function AcceptButton({
  inviteId,
  challengeId,
}: AcceptButtonProps) {
  async function handleClick() {
    const { success } = await acceptInvite(inviteId);
    if (!success) {
      toastGenericError();
      return;
    }
    redirect(`/challenges/${challengeId}`);
  }
  return <Button onClick={handleClick}>Accept</Button>;
}
